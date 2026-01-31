package com.mike.taskmaster.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mike.taskmaster.dto.OrganizationResponseDTO;
import com.mike.taskmaster.dto.TaskRequestDTO;
import com.mike.taskmaster.dto.TaskResponseDTO;
import com.mike.taskmaster.entity.User;
import com.mike.taskmaster.security.jwt.JwtTokenProvider;
import com.mike.taskmaster.service.OrganizationService;
import com.mike.taskmaster.service.TaskService;
import com.mike.taskmaster.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;





@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final OrganizationService organizationService;

    public TaskController(TaskService taskService, JwtTokenProvider jwtTokenProvider, UserService userService, OrganizationService organizationService) {
        this.taskService = taskService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.organizationService = organizationService;
    }
    @Operation(summary = "Returns a task associated with given UUID")
    @GetMapping("{id}")
    public ResponseEntity<TaskResponseDTO> get(@PathVariable UUID id) {

        return ResponseEntity.ok().body(taskService.getTask(id));
    }

    @Operation(summary = "Creates a task")
    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@CookieValue(name = "accessToken", required = false) String token,  @RequestBody TaskRequestDTO dto) {
        UUID id = jwtTokenProvider.parseTokenForUUID(token);
        User user = userService.getUserEntity(id);


        List<User> members = userService.getUsersEntities(dto.getAssignees());
        TaskResponseDTO task = taskService.createTask(dto, user, members);
        
        return ResponseEntity.ok(task);
    }


    @Operation(summary = "Deletes a task")
    @DeleteMapping("{id}")
    public ResponseEntity<TaskResponseDTO> softDelete(@CookieValue(name = "accessToken", required = false) String token, @PathVariable UUID id) {
        UUID userId = jwtTokenProvider.parseTokenForUUID(token);
        User user = userService.getUserEntity(userId);

        TaskResponseDTO task = taskService.softDelete(id, user);
        return ResponseEntity.ok().body(task);
    }
}
