package com.mike.taskmaster.mapper;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.mike.taskmaster.dto.TaskRequestDTO;
import com.mike.taskmaster.dto.UserRequestDTO;
import com.mike.taskmaster.entity.Task;
import com.mike.taskmaster.entity.User;
import com.mike.taskmaster.service.UserService;



import jakarta.transaction.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional

public class TaskMapperTest {

    @Autowired
    private UserService userService;
    private User jane;
    private User frank;

    @BeforeEach
    public void setUp() {
        UserRequestDTO dto1= new UserRequestDTO("jane", "jane@example.com", "Simpl3Passw0rdz", false);
        UserRequestDTO dto2 = new UserRequestDTO("frank", "frank@example.com", "Simpl3Passw0rdz", false);
        this.jane = userService.createUser(dto1);
        this.frank = userService.createUser(dto2);
    }

    @Test
    public void testToEntity() {
        Set<User> assignees = new HashSet<>();
        assignees.add(frank);
        TaskRequestDTO dto = new TaskRequestDTO("test title", "some task description", Collections.singleton(frank.getId()), null, null, null, null);
        Task mappedTask = TaskMapper.toEntity(dto, jane, assignees);
        assertThat(mappedTask).isNotNull();
        assertThat(mappedTask.getAssignees()).extracting(a -> a.getName(), a -> a.getEmail()).contains(tuple("frank", "frank@example.com"));
        assertThat(mappedTask.getCreator()).isEqualTo(jane);
        assertThat(mappedTask.getDescription()).isEqualTo(dto.getDescription());
        assertThat(mappedTask.getTitle()).isEqualTo(dto.getTitle());
        

        
    }
}
