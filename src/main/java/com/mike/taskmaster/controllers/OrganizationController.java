package com.mike.taskmaster.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mike.taskmaster.dto.OrganizationRequestDTO;
import com.mike.taskmaster.dto.OrganizationResponseDTO;
import com.mike.taskmaster.entity.User;
import com.mike.taskmaster.security.jwt.JwtTokenProvider;
import com.mike.taskmaster.service.OrganizationService;
import com.mike.taskmaster.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("organization")
public class OrganizationController {
    private final OrganizationService organizationService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    public OrganizationController(OrganizationService organizationService, JwtTokenProvider jwtTokenProvider, UserService userService ) {
        this.organizationService = organizationService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }
    @Operation(summary = "Creates a new organization")
    @PostMapping
    public ResponseEntity<OrganizationResponseDTO> createOrganization(@CookieValue(name = "accessToken", required = false) String token, @RequestBody OrganizationRequestDTO dto) {
        UUID id = jwtTokenProvider.parseTokenForUUID(token);
        User user = userService.getUserEntity(id);
        
        List<User> members = userService.getUsersEntities(dto.getMembers());

        OrganizationResponseDTO org = organizationService.createOrganizationWithOwner(dto, user, members);
        
        return ResponseEntity.ok(org);
    }

    @Operation(summary = "Returns an organization associated with provided id")
    @GetMapping("{orgId}")
    public ResponseEntity<OrganizationResponseDTO> getOrganization(@PathVariable UUID orgId) {
        return ResponseEntity.ok().body(organizationService.getOrganization(orgId));
    }
            // TODO: transform to request/invite flow - calls for a new domain entities and service
    @Operation(summary = "Adds a user to the organization | will be transformed to request in future")
    @PostMapping("/{orgId}/members")
    public ResponseEntity<OrganizationResponseDTO> joinOrganization(@CookieValue(name = "accessToken", required = false) String token, @PathVariable UUID orgId) {
        UUID id = jwtTokenProvider.parseTokenForUUID(token);
        User user = userService.getUserEntity(id);
        OrganizationResponseDTO updatedOrg = organizationService.addMember(orgId, user);
        return ResponseEntity.ok().body(updatedOrg);
    }
      // TODO: transform to request/invite flow - calls for a new domain entities and service
    @Operation(summary = "Removes a user from the organization | will be transformed to request in future")
    @DeleteMapping("/{orgId}/members")
    public ResponseEntity<OrganizationResponseDTO> leaveOrganization(@CookieValue(name = "accessToken", required = false) String token, @PathVariable UUID orgId) {
        UUID id = jwtTokenProvider.parseTokenForUUID(token);
        User user = userService.getUserEntity(id);
        OrganizationResponseDTO updatedOrg = organizationService.removeMember(orgId, user);
        return ResponseEntity.ok().body(updatedOrg);
    }
    
}
