package com.mike.taskmaster.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.mike.taskmaster.dto.UserRequestDTO.NAME_MIN_MSG;
import static com.mike.taskmaster.dto.UserRequestDTO.EMAIL_INVALID_MSG;
import static com.mike.taskmaster.dto.UserRequestDTO.PASSWORD_INVALID_MSG;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

public class UserRequestDTOTest {
    private Validator validator;
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNameMinLength() {
        UserRequestDTO dto = new UserRequestDTO("to", "tom@example.com", "Amazing", null);
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("name") && 
            v.getMessage().equals(NAME_MIN_MSG));
    }

    @Test
    void testEmailPattern() {
        UserRequestDTO dto = new UserRequestDTO("tom", "tom#example", "Amazing", null);
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v ->
            v.getPropertyPath().toString().equals("email") &&
            v.getMessage().equals(EMAIL_INVALID_MSG));
    }

    @Test
    void testPasswordPattern() {
        UserRequestDTO dto = new UserRequestDTO("tom", "tom@example.com", "mazing", null);
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("password") && 
            v.getMessage().equals(PASSWORD_INVALID_MSG));
    }

    @Test
    void testValidDTO() {
        UserRequestDTO dto = new UserRequestDTO("tom", "tom@example.com", "Amazing", null);
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }
}
