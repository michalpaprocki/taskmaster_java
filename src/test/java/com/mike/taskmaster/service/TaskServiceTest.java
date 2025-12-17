package com.mike.taskmaster.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.mike.taskmaster.dto.TaskRequestDTO;
import com.mike.taskmaster.dto.TaskResponseDTO;
import com.mike.taskmaster.dto.UserRequestDTO;
import com.mike.taskmaster.dto.UserResponseDTO;
import com.mike.taskmaster.entity.Task;
import com.mike.taskmaster.entity.Task.Status;
import com.mike.taskmaster.entity.User;

import jakarta.transaction.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class TaskServiceTest {
    
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    private User jane;
    private User frank;
    private TaskResponseDTO mockupTask;

    @BeforeEach
    void setUp() {
        UserRequestDTO dtoJane = new UserRequestDTO("jane", "jane@example.com", "Amazing", null);
        UserRequestDTO dtoFrank = new UserRequestDTO("frank", "frank@example.com", "Amazing", null);
        this.jane = userService.createUser(dtoJane);
        this.frank = userService.createUser(dtoFrank);
        TaskRequestDTO taskDto = new TaskRequestDTO("mockup task title", "mockup task decription", null, TaskRequestDTO.Action.ADD, false, Status.PENDING, null);
   
        this.mockupTask = taskService.createTask(taskDto, jane);
    }

    @Test
    void testCreateTask() {
        TaskRequestDTO dto = new TaskRequestDTO("test task", "Some description for testing", null, TaskRequestDTO.Action.ADD, false, Status.PENDING, null);
   
        TaskResponseDTO task = taskService.createTask(dto, frank);

        assertThat(task).isNotNull();
        assertThat(task.getTitle()).isEqualTo(dto.getTitle());
        assertThat(task.getDescription()).isEqualTo(dto.getDescription());
        assertThat(task.getCreator().getId()).isEqualTo(frank.getId());
        assertThat(task).isInstanceOf(TaskResponseDTO.class);
    }

    @Test
    void testGetTask() {
        Task task = taskService.getTaskEntity(mockupTask.getId());
        assertThat(task).isNotNull();
        assertThat(task.getTitle()).isEqualTo(mockupTask.getTitle());
        assertThat(task.getDescription()).isEqualTo(mockupTask.getDescription());
        assertThat(task.getCreator().getName()).isEqualTo(mockupTask.getCreator().getName());
        assertThat(task).isInstanceOf(Task.class);
    }

    @Test
    void testUpdateTask() {
        LocalDateTime now = LocalDateTime.now();
        Set<UUID> assignees = new HashSet<>();
        assignees.add(frank.getId());
        TaskRequestDTO dto = new TaskRequestDTO("my new testing title", "Just a new description", null, TaskRequestDTO.Action.ADD, true, Task.Status.CANCELED, now);
        TaskResponseDTO task = taskService.updateTask(mockupTask.getId(), dto, jane, assignees);
        assertThat(task).isNotNull();
        assertThat(task.getTitle()).isEqualTo(dto.getTitle());
        assertThat(task.getDescription()).isEqualTo(dto.getDescription());
        assertThat(task.getAssignees()).extracting(UserResponseDTO::getName, UserResponseDTO::getEmail).contains(tuple("frank", "frank@example.com"));
        assertThat(task.getStatus()).isEqualTo(Task.Status.CANCELED);
        assertThat(task.getDeadline()).isEqualTo(now);
        
    }

    @Test 
    public void testIsTaskCreator() {
        boolean isCreator = taskService.isCreator(mockupTask.getId(), jane.getId());
        assertTrue(isCreator);
    }

    @Test 
    public void testIsNotTaskCreator() {
        boolean isCreator = taskService.isCreator(mockupTask.getId(), UUID.randomUUID());
        assertFalse(isCreator);
    }
}
