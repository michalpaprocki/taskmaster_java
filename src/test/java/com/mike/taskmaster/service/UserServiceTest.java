package com.mike.taskmaster.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mike.taskmaster.repository.UserRepository;
import com.mike.taskmaster.entity.User;


@SpringBootTest
@Transactional
public class UserServiceTest {
    
    @BeforeEach
    void cleanDb(){
        userRepository.deleteAll();
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreateUser() {
        String name = "jane";
        String email = "jane@example.com";

        User user = userService.createUser(name, email);

        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(userRepository.existsById(user.getId())).isTrue();

    }

    @Test
    void testDuplicateEmailThrows() {
        String email = "frank@example.com";
        userService.createUser("frank", email);

        assertThatThrownBy(()-> userService.createUser("tony", email)).isInstanceOf(IllegalArgumentException.class).hasMessage("Email already taken");
    }
}
