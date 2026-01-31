package com.mike.taskmaster.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mike.taskmaster.dto.OrganizationResponseDTO;
import com.mike.taskmaster.security.jwt.JwtTokenProvider;
import com.mike.taskmaster.service.OrganizationService;
import com.mike.taskmaster.service.UserService;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/organizations")
public class OrganizationsController {
    private final OrganizationService organizationService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    public OrganizationsController(OrganizationService organizationService, JwtTokenProvider jwtTokenProvider, UserService userService ) {
        this.organizationService = organizationService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Operation(summary = "Returns all organizations")
    @GetMapping
    public ResponseEntity<List<OrganizationResponseDTO>> getAllOrganizations() {
        List<OrganizationResponseDTO> dtos = organizationService.getAllOrganizations();
        return ResponseEntity.ok().body(dtos);
    }

}
