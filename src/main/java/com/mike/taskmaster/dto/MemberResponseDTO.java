package com.mike.taskmaster.dto;

import java.util.UUID;

import com.mike.taskmaster.entity.Membership;

public class MemberResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private Membership.Role role;

    public MemberResponseDTO(Membership membership) {
        this.id = membership.getUser().getId();
        this.name = membership.getUser().getName();
        this.email = membership.getUser().getEmail();
        this.role = membership.getRole();
    }

    public UUID getId() {
        return id;
    }
    public String getName() {return name;}
    public String getEmail() {return email;}
    public Membership.Role getRole() {return role;}

    public void setId(UUID id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setRole(Membership.Role role) {
        this.role = role;
    }
}
