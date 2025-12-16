package com.mike.taskmaster.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;


import com.mike.taskmaster.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import com.mike.taskmaster.dto.UserRequestDTO;
import com.mike.taskmaster.entity.User;



@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserServiceTest {


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private User jane;

    @BeforeEach
    void cleanAndSetupDb(){

        UserRequestDTO janeDTO = new UserRequestDTO("jane", "jane@example.com", "Amazing", null);

        
        this.jane = userService.createUser(janeDTO);
    }

    @Test
    void testCreateUser() {
        UserRequestDTO dto = new UserRequestDTO("tony", "tony@example.com", "Amazing", null);
        User user = userService.createUser(dto);
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("tony");
        assertThat(user.getEmail()).isEqualTo("tony@example.com");
        assertThat(userRepository.existsById(user.getId())).isTrue();
    }

    @Test
    void testDuplicateEmailThrows() {
        UserRequestDTO dto = new UserRequestDTO("tony", jane.getEmail(), jane.getPassword(),null);
        assertThatThrownBy(()-> userService.createUser(dto)).isInstanceOf(IllegalArgumentException.class).hasMessage("Email already taken");
    }

    @Test
    void testGetUser() {
        User user = userService.getUserEntity(jane.getId());
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo(jane.getName());
        assertThat(user.getEmail()).isEqualTo(jane.getEmail());
    }

    @Test
    void testMissingUserThrows() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> userService.getUser(id)).isInstanceOf(EntityNotFoundException.class).hasMessage("User not found");
    }

    @Test
    void testGetEmail() {
        String email = userService.getEmail(jane.getId());
        assertThat(email).isNotNull();
        assertThat(email).isEqualTo(jane.getEmail());
    }

    @Test
    void testUserUpdate() {
        UserRequestDTO dto = new UserRequestDTO("cassandra", "cassandra@example.com", "my_secr3t_Passwordz", null);
        User user = userService.updateUser(jane.getId(), dto);
        assertThat(user.getName()).isEqualTo("cassandra");
        assertThat(user.getEmail()).isEqualTo("cassandra@example.com");
        assertNotEquals(dto.getPassword(), user.getPassword());
        assertTrue(passwordEncoder.matches(dto.getPassword(), user.getPassword()));
    }

    @Test
    void testUserSoftDelete() {
        String responseString = userService.softDeleteUser(jane.getId());
        assertThat(responseString).isEqualTo("User deleted successfully");
        User user = userService.getUserEntity(jane.getId());
        assertThat(user).isNotNull();
        assertThat(user.getIsDeleted()).isEqualTo(true);
        assertThat(user.getDeletedAt()).isInstanceOf(LocalDateTime.class);
    }
    void testUserHardDelete() {
        String responseString = userService.hardDeleteUser(jane.getId());
        assertThat(responseString).isEqualTo("User deleted successfully");
        assertThatThrownBy(() -> userService.getUser(jane.getId())).isInstanceOf(EntityNotFoundException.class).hasMessage("User not found");
    }
}
