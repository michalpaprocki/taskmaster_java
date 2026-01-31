package com.mike.taskmaster.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import com.mike.taskmaster.dto.UserResponseDTO;
import com.mike.taskmaster.dto.ChangePasswordRequestDTO;
import com.mike.taskmaster.exception.JwtValidationException;
import com.mike.taskmaster.security.jwt.JwtTokenProvider;
import com.mike.taskmaster.service.UserService;
import io.swagger.v3.oas.annotations.Operation;



@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(JwtTokenProvider jwtTokenProvider, UserService userService){
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

        
    
    @Operation(summary = "Returns current user's data")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(@CookieValue(name="accessToken", required = false) String token) {
        if (token == null) {
            throw new JwtValidationException("Missing jwt token");
        }
            UUID id = jwtTokenProvider.parseTokenForUUID(token);

            UserResponseDTO user = userService.getUser(id);

            return ResponseEntity.ok(user);
        }

    @Operation(summary = "Changes user password")
    @PatchMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody ChangePasswordRequestDTO dto, @CookieValue(name = "accessToken", required = false) String token){
          if (token == null) {
            throw new JwtValidationException("Missing jwt token");
        }
            UUID id = jwtTokenProvider.parseTokenForUUID(token);

            userService.changePassword(id, dto);
    }

    @Operation(summary = "Returns all the users")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>>  getUsers() {
        List<UserResponseDTO> dtos = userService.getAllUsers();
        return ResponseEntity.ok().body(dtos);
    }
    
}
