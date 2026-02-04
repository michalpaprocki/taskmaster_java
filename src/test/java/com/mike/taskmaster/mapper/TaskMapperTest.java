package com.mike.taskmaster.mapper;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mike.taskmaster.dto.TaskRequestDTO;
import com.mike.taskmaster.entity.Task;
import com.mike.taskmaster.entity.User;





public class TaskMapperTest {


    private User jane;
    private User frank;
    private Task testTask;
    List<UUID> uuids = new ArrayList<>();
    List<User> assignees = new ArrayList<>();
    @BeforeEach
    public void setUp() {
        this.jane = new User("jane", "jane@example.com", "Simpl3Passw0rdz", false);
        jane.setId(UUID.randomUUID());
        this.frank = new User("frank", "frank@example.com", "Simpl3Passw0rdz", false);
        frank.setId(UUID.randomUUID());
        this.assignees.add(frank);
        this.testTask = new Task();
        testTask.setTitle("Testing task");
        testTask.setDescription("Short task description");
        testTask.setCreator(jane);

    }

    @Test
    public void testToEntity() {
  

        TaskRequestDTO dto = new TaskRequestDTO("test title", "some task description", uuids, null, null, null, null, null);
        Task mappedTask = TaskMapper.toEntity(dto, jane, assignees, null);
        assertThat(mappedTask).isNotNull();
        assertThat(mappedTask.getAssignees()).extracting(a -> a.getName(), a -> a.getEmail()).contains(tuple("frank", "frank@example.com"));
        assertThat(mappedTask.getCreator()).isEqualTo(jane);
        assertThat(mappedTask.getDescription()).isEqualTo(dto.getDescription());
        assertThat(mappedTask.getTitle()).isEqualTo(dto.getTitle());
    }

    @Test
    public void testUpdateEntity() {

        assignees.add(jane);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = now.plusDays(30);

        TaskRequestDTO dto = new TaskRequestDTO("New test title", "New updated description", null, null, TaskRequestDTO.Action.ADD, null, null, deadline);
        Task updatedTask = TaskMapper.updateEntity(testTask, dto, frank, assignees, null);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getTitle()).isEqualTo("New test title");
        assertThat(updatedTask.getDescription()).isEqualTo("New updated description");
        assertThat(updatedTask.getAssignees()).extracting(a -> a.getName(), a -> a.getEmail()).contains(tuple("frank", "frank@example.com"));
        assertThat(updatedTask.getAssignees()).extracting(a -> a.getName(), a -> a.getEmail()).contains(tuple("jane", "jane@example.com"));
        assertThat(updatedTask.getStatus()).isEqualTo(Task.Status.PENDING);
        assertThat(updatedTask.getDeadline()).isEqualTo(deadline);
    }

}
