package com.mike.taskmaster.dto;

import java.util.UUID;

public class UserResponseDTO {
    private UUID id;
    private String name;
    private String email;

    public UserResponseDTO() {}

    public UserResponseDTO(UUID id, String name, String email) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public UUID getId() {return id;}
    public String getName() {return name;}
    public String getEmail() {return email;}

    public void setId(UUID id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
