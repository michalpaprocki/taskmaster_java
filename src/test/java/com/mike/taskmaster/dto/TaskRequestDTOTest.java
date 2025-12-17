package com.mike.taskmaster.dto;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mike.taskmaster.entity.User;
import com.mike.taskmaster.entity.Task.Status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static com.mike.taskmaster.dto.TaskRequestDTO.DESCRIPTION_MIN_MSG;
import static com.mike.taskmaster.dto.TaskRequestDTO.DESCRIPTION_REQUIRED_MSG;
import static com.mike.taskmaster.dto.TaskRequestDTO.TITLE_MIN_MSG;
import static com.mike.taskmaster.dto.TaskRequestDTO.TITLE_REQUIRED_MSG;
import static org.assertj.core.api.Assertions.assertThat;

public class TaskRequestDTOTest {
     
    private Validator validator;
    private User jane;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        // UserRequestDTO userDto = new UserRequestDTO("jane", "jane@example.com", "MySecr3tPassw0rd", null);
        
        this.jane = new User();
        jane.setName("jane");
        jane.setEmail("jane@example.com");
        jane.setPassword("MySecr3tPasswr0d");
        jane.setIsDeleted(false);
    }

    @Test
    void testTitleMinLength() {

        TaskRequestDTO dto = new TaskRequestDTO("to", "some description to satisfy the requirements", null, TaskRequestDTO.Action.ADD, false, Status.PENDING, null);
        Set<ConstraintViolation<TaskRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("title") && 
            v.getMessage().equals(TITLE_MIN_MSG));
    }

    @Test
    void testTitleNotBlank() {
        TaskRequestDTO dto = new TaskRequestDTO("", "some description to satisfy the requirements", null, TaskRequestDTO.Action.ADD, false, Status.PENDING, null);
        Set<ConstraintViolation<TaskRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("title") && 
            v.getMessage().equals(TITLE_REQUIRED_MSG));
    }

    @Test
    void testDescriptionMinLength() {

        TaskRequestDTO dto = new TaskRequestDTO("task name to satisfy the requirements", "description", null, TaskRequestDTO.Action.ADD, false, Status.PENDING, null);
        Set<ConstraintViolation<TaskRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("description") && 
            v.getMessage().equals(DESCRIPTION_MIN_MSG));
    }

    @Test
    void testDescriptionNotBlank() {

        TaskRequestDTO dto = new TaskRequestDTO("task name to satisfy the requirements", "", null,TaskRequestDTO.Action.ADD, false, Status.PENDING, null);
        Set<ConstraintViolation<TaskRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> 
            v.getPropertyPath().toString().equals("description") && 
            v.getMessage().equals(DESCRIPTION_REQUIRED_MSG));
    }

}
