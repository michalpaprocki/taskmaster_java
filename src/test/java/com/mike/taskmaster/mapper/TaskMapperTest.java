package com.mike.taskmaster.mapper;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.Collections;
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
import com.mike.taskmaster.entity.Task;
import com.mike.taskmaster.entity.User;
import com.mike.taskmaster.service.TaskService;
import com.mike.taskmaster.service.UserService;



import jakarta.transaction.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional

public class TaskMapperTest {

    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;
    private User jane;
    private User frank;
    private TaskResponseDTO testTask;

    @BeforeEach
    public void setUp() {
        UserRequestDTO dto1= new UserRequestDTO("jane", "jane@example.com", "Simpl3Passw0rdz", false);
        UserRequestDTO dto2 = new UserRequestDTO("frank", "frank@example.com", "Simpl3Passw0rdz", false);
        this.jane = userService.createUser(dto1);
        this.frank = userService.createUser(dto2);

        TaskRequestDTO taskDto = new TaskRequestDTO("Testing task", "Short task description", null,null, null, null, null);
        this.testTask = taskService.createTask(taskDto, frank);
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

    @Test
    public void testUpdateEntity() {
        Set<UUID> assignees = new HashSet<>();
        assignees.add(jane.getId());
        assignees.add(frank.getId());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = now.plusDays(30);

        TaskRequestDTO dto = new TaskRequestDTO("New test title", "New updated description", assignees, TaskRequestDTO.Action.ADD, null, null, deadline);
        TaskResponseDTO updatedTask = taskService.updateTask(testTask.getId(), dto, frank, assignees);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getTitle()).isEqualTo("New test title");
        assertThat(updatedTask.getDescription()).isEqualTo("New updated description");
        assertThat(updatedTask.getAssignees()).extracting(a -> a.getName(), a -> a.getEmail()).contains(tuple("frank", "frank@example.com"));
        assertThat(updatedTask.getAssignees()).extracting(a -> a.getName(), a -> a.getEmail()).contains(tuple("jane", "jane@example.com"));
        assertThat(updatedTask.getStatus()).isEqualTo(Task.Status.PENDING);
        assertThat(updatedTask.getDeadline()).isEqualTo(deadline);
    }

    @Test
    public void testUpdateEntityThrows() {
        Set<UUID> assignees = new HashSet<>();
        UUID randomId1 = UUID.randomUUID();
        UUID randomId2 = UUID.randomUUID();
        assignees.add(randomId1);
        assignees.add(randomId2);
        TaskRequestDTO dto = new TaskRequestDTO(null, null, assignees, TaskRequestDTO.Action.ADD, null, null, null);

        assertThatThrownBy(() -> taskService.updateTask(testTask.getId(), dto, frank, assignees)).isInstanceOf(IllegalArgumentException.class).hasMessage("Some users not found");
    }
}
