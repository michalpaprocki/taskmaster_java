package com.mike.taskmaster.service;

import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mike.taskmaster.dto.TaskRequestDTO;
import com.mike.taskmaster.dto.TaskResponseDTO;
import com.mike.taskmaster.entity.Task;
import com.mike.taskmaster.entity.User;
import com.mike.taskmaster.mapper.TaskMapper;
import com.mike.taskmaster.repository.TaskRepository;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;
    protected final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;    }

    public TaskResponseDTO createTask(TaskRequestDTO dto, User creator) {
        Set<User> assignees = userService.getUserEntities(dto.getAssignees());
        Task task = TaskMapper.toEntity(dto, creator, assignees);
        taskRepository.save(task);
        return new TaskResponseDTO(task);
    }

    public TaskResponseDTO getTask(UUID id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return TaskMapper.toResponse(task);
    }
    public Task getTaskEntity(UUID id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return task;
    }

    public TaskResponseDTO updateTask(UUID id, TaskRequestDTO dto, User creator, Set<User> assignees) {
        Task task = getTaskEntity(id);
        
        TaskMapper.updateEntity(task, dto, creator, assignees);
        Task updatedTask = taskRepository.save(task);
        return new TaskResponseDTO(updatedTask);
    }
    // public TaskResponseDTO softRemoveTask(UUID id, TaskRequestDTO dto) {
    //     Task task = getTaskEntity(id);

    // }
    public TaskResponseDTO hardRemoveTask(UUID id) {
        Task targetTask = getTaskEntity(id);
        taskRepository.deleteById(id);
        return new TaskResponseDTO(targetTask);
    }
}
