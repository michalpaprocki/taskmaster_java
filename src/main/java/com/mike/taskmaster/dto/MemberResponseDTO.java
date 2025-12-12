package com.mike.taskmaster.dto;

import java.util.UUID;

import com.mike.taskmaster.entity.Membership;

public class MemberResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String role;

    public MemberResponseDTO(Membership membership) {
        this.id = membership.getUser().getId();
        this.name = membership.getUser().getName();
        this.email = membership.getUser().getEmail();
        this.role = membership.getRole().name();
    }

    public UUID getId() {
        return id;
    }
    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getRole() {return role;}
    public void setRole(UUID id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
