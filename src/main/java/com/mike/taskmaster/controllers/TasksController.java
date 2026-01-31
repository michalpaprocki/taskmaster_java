package com.mike.taskmaster.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mike.taskmaster.dto.TaskResponseDTO;
import com.mike.taskmaster.security.jwt.JwtTokenProvider;
import com.mike.taskmaster.service.OrganizationService;
import com.mike.taskmaster.service.TaskService;
import com.mike.taskmaster.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/tasks")
public class TasksController {
    

    private final TaskService taskService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final OrganizationService organizationService;

    public TasksController(TaskService taskService, JwtTokenProvider jwtTokenProvider, UserService userService, OrganizationService organizationService) {
        this.taskService = taskService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.organizationService = organizationService;
    }

    @Operation(summary = "Returns all tasks")
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> get() {
        List<TaskResponseDTO> tasks = taskService.getAllTasks();

        return ResponseEntity.ok().body(tasks);
    }
}
