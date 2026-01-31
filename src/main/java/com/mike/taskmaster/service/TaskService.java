package com.mike.taskmaster.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
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

    public TaskResponseDTO createTask(TaskRequestDTO dto, User creator, List<User> assignees) {
        if (taskRepository.existsByTitle(dto.getTitle())){
            throw new IllegalArgumentException("Task name already taken");
        }
        
        Task taskEntity = TaskMapper.toEntity(dto, creator, assignees);
         return TaskMapper.toResponse(taskRepository.save(taskEntity));
    }

    public TaskResponseDTO getTask(UUID id) {
        Task task = taskRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return TaskMapper.toResponse(task);
    }
    public Task getTaskEntity(UUID id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return task;
    }
    
    public List<TaskResponseDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findByAndIsDeletedFalse();
        List<TaskResponseDTO> dtos = tasks.stream().map(t -> TaskMapper.toResponse(t)).collect(Collectors.toList());
        return dtos;
    }
    public boolean isCreator(UUID id, UUID userId) {
        Task task = getTaskEntity(id);
        if (task.getCreator().getId().equals(userId)) {
            return true;
        } else{
            return false;
        }
    }

    public TaskResponseDTO updateTask(UUID id, TaskRequestDTO dto, User creator, List<UUID> assignees) {
        Task task = getTaskEntity(id);
        if (!isCreator(id, creator.getId())) {
            throw new IllegalArgumentException("Provided user is not creator of the task");
        }
        List<User> users = userService.getUserEntities(assignees);

        if (users.size() != assignees.size()){
            throw new IllegalArgumentException("Some users not found");
        }

        TaskMapper.updateEntity(task, dto, creator, users);
        Task updatedTask = taskRepository.save(task);
        return new TaskResponseDTO(updatedTask);
    }
    
    public TaskResponseDTO hardRemoveTask(UUID id) {
        Task targetTask = getTaskEntity(id);
        taskRepository.deleteById(id);
        return new TaskResponseDTO(targetTask);
    }
    public TaskResponseDTO softDelete(UUID id, User user) {
        Task task = getTaskEntity(id);
        if (task.getCreator().getId() != user.getId()) {
            throw new AccessDeniedException("Not authorized"); 
        }
        task.setIsDeleted(true);
        taskRepository.save(task);
        return TaskMapper.toResponse(task);
    }
}
