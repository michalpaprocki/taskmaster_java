package com.mike.taskmaster.dto;

import java.util.UUID;

import com.mike.taskmaster.entity.User;

public class UserResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private Boolean isDeleted;

    public UserResponseDTO() {}

    public UserResponseDTO(UUID id, String name, String email, Boolean isDeleted) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.isDeleted = isDeleted;
    }
    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.isDeleted = user.getIsDeleted();
    }

    public UUID getId() {return id;}
    public String getName() {return name;}
    public String getEmail() {return email;}
    public Boolean getIsDeleted() {return isDeleted;}

    public void setId(UUID id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
