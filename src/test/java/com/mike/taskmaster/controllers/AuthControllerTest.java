package com.mike.taskmaster.controllers;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mike.taskmaster.dto.UserRequestDTO;
import com.mike.taskmaster.entity.User;
import com.mike.taskmaster.security.jwt.JwtTokenProvider;
import com.mike.taskmaster.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    private UserRequestDTO jane = new UserRequestDTO("jane", "jane@example.com", "my secr3t pAssw0rdz", null);

    @Test
    void registerSuccess() throws Exception {
        UserRequestDTO request = jane;
        User user = new User("jane", "jane@example.com","my_secret_password", false);
        user.setId(UUID.randomUUID());

        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(user);

        mockMvc.perform(post("/auth/register")
            
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated()).andDo(print())
            .andExpect(jsonPath("$.name").value(user.getName()))
            .andExpect(jsonPath("$.email").value(user.getEmail()));
        }

    @Test
    void registerUserAlreadyExists() throws Exception {
         UserRequestDTO request = new UserRequestDTO("jane", "jane@example.com", "my secr3t pAssw0rdz", null);
            userService.createUser(request);

        when(userService.createUser(any(UserRequestDTO.class)))
            .thenThrow(new DataIntegrityViolationException("duplicate key"));

         mockMvc.perform(post("/auth/register")
         .contentType(MediaType.APPLICATION_JSON)
         .content(objectMapper.writeValueAsString(request)))
         .andExpect(status().isBadRequest())
         .andExpect(jsonPath("$.message").value("Email or name already taken"));
    }
    
    
    @Test
    void loginSuccess() throws Exception {
        User user = new User("jane", "jane@example.com", "hashed_password", false);
        user.setId(UUID.randomUUID());

        when(userService.login(user.getEmail(), user.getPassword()))
            .thenReturn(user);
        when(jwtTokenProvider.generateToken(user))
            .thenReturn("mocked_jwt_token");

        UserRequestDTO loginDto = new UserRequestDTO(user.getName(), user.getEmail(), user.getPassword(), null);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
            .andExpect(status().isOk())
            .andExpect(cookie().exists("JWT_TOKEN"))
            .andExpect(cookie().value("JWT_TOKEN", "mocked_jwt_token"))
            .andExpect(cookie().httpOnly("JWT_TOKEN", true))
            .andExpect(cookie().secure("JWT_TOKEN", true));
    }

    @Test
    void loginFailWrongPassword() throws Exception {
       String badPassword = "bad_pass";
        UserRequestDTO dto = new UserRequestDTO("jane", "jane@example.com", badPassword, false);

       when(userService.login(dto.getEmail(), badPassword)).thenThrow(new RuntimeException("Invalid email or password"));


       mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }
}   
