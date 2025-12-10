package com.mike.taskmaster.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;


import static com.mike.taskmaster.dto.UserRequestDTO.EMAIL_INVALID_MSG;
import static com.mike.taskmaster.dto.UserRequestDTO.PASSWORD_INVALID_MSG;

public class UserUpdateDTOTest {
    private Validator validator;
    private UUID id = UUID.randomUUID();
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    void testEmailPattern() {
        UserUpdateDTO dto = new UserUpdateDTO(id, "tom", "tom#example", "Amazing");
        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v ->
            v.getPropertyPath().toString().equals("email") &&
            v.getMessage().equals(EMAIL_INVALID_MSG));
    }

    @Test
    void testPasswordPattern() {
        UserUpdateDTO dto = new UserUpdateDTO(id, "tom", "tom@example.com", "mazing");
        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("password") && 
            v.getMessage().equals(PASSWORD_INVALID_MSG));
    }

    @Test
    void testValidDTO() {
        UserUpdateDTO dto = new UserUpdateDTO(id, "tom", "tom@example.com", "Amazing");
        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }
}

