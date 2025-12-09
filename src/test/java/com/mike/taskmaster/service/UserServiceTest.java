package com.mike.taskmaster.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;


import com.mike.taskmaster.repository.UserRepository;
import com.mike.taskmaster.entity.User;



@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserServiceTest {


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User jane;

    @BeforeEach
    void cleanAndSetupDb(TestInfo testInfo){

        jane = new User("jane", "jane@example.com");

        userRepository.save(jane);
    }

    @Test
    void testCreateUser() {
        User user = userService.createUser("tony", "tony@example.com");
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("tony");
        assertThat(user.getEmail()).isEqualTo("tony@example.com");
        assertThat(userRepository.existsById(user.getId())).isTrue();
    }

    @Test
    void testDuplicateEmailThrows() {

        assertThatThrownBy(()-> userService.createUser("tony", jane.getEmail())).isInstanceOf(IllegalArgumentException.class).hasMessage("Email already taken");
    }

    @Test
    void testGetUser() {
        User user = userService.getUser(jane.getId());
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo(jane.getName());
        assertThat(user.getEmail()).isEqualTo(jane.getEmail());
    }

    @Test
    void testMissingUserThrows() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> userService.getUser(id)).isInstanceOf(IllegalArgumentException.class).hasMessage("User not found");
    }

    @Test
    void testGetEmail() {
        String email = userService.getEmail(jane.getId());
        assertThat(email).isNotNull();
        assertThat(email).isEqualTo(jane.getEmail());
    }
}
