package com.mike.taskmaster.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static com.mike.taskmaster.dto.OrganizationRequestDTO.DESC_MIN_MSG;
import static com.mike.taskmaster.dto.OrganizationRequestDTO.NAME_MIN_MSG;
import static com.mike.taskmaster.dto.OrganizationRequestDTO.NAME_REQUIRED_MSG;;

public class OrganizationRequestDTOTest {
    private Validator validator;
    private List<UUID> uuids = new ArrayList<>();
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNameMinLength() {
        OrganizationRequestDTO dto = new OrganizationRequestDTO("Wa", null, uuids, OrganizationRequestDTO.Action.ADD);
        Set<ConstraintViolation<OrganizationRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("name") && 
            v.getMessage().equals(NAME_MIN_MSG));
    }

    @Test
    void testNameNotBlank() {
        OrganizationRequestDTO dto = new OrganizationRequestDTO("", "", uuids, OrganizationRequestDTO.Action.ADD);
        Set<ConstraintViolation<OrganizationRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("name") && 
            v.getMessage().equals(NAME_REQUIRED_MSG));
    }

    @Test
    void testValidDTO() {
        OrganizationRequestDTO dto = new OrganizationRequestDTO("Wayland", "my organization description", uuids, OrganizationRequestDTO.Action.ADD);
        Set<ConstraintViolation<OrganizationRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void testDescriptionMinLength() {
        OrganizationRequestDTO dto = new OrganizationRequestDTO("Wayland", "my", uuids, OrganizationRequestDTO.Action.ADD);
        Set<ConstraintViolation<OrganizationRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v ->
            v.getPropertyPath().toString().equals("description") && 
            v.getMessage().equals(DESC_MIN_MSG));
    }
}
