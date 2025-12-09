package com.mike.taskmaster.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mike.taskmaster.dto.UserDTO;
import static com.mike.taskmaster.dto.UserDTO.NAME_MIN_MSG;
import static com.mike.taskmaster.dto.UserDTO.EMAIL_INVALID_MSG;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

public class UserDTOTest {
    private Validator validator;
 
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNameMinLength() {
        UserDTO dto = new UserDTO("to", "tom@example.com");
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("name") && 
            v.getMessage().equals(NAME_MIN_MSG));
    }

    @Test
    void testEmailPattern() {
        UserDTO dto = new UserDTO("tom", "tom#example");
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v ->
            v.getPropertyPath().toString().equals("email") &&
            v.getMessage().equals(EMAIL_INVALID_MSG));
    }

    @Test
    void testValidDTO() {
        UserDTO dto = new UserDTO("tom", "tom@example.com");
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }
}
