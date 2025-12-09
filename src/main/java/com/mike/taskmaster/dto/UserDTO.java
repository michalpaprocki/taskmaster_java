package com.mike.taskmaster.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO {
    
    public static final String NAME_MIN_MSG = "Name must be at least 3 characters long";
    public static final String NAME_REQUIRED_MSG = "Name is required";
    public static final String EMAIL_REQUIRED_MSG = "Email is required";
    public static final String EMAIL_INVALID_MSG = "Email must be valid";

    @NotBlank(message = NAME_REQUIRED_MSG)
    @Size(min = 3, message = NAME_MIN_MSG)
    private String name;

    @NotBlank(message = EMAIL_REQUIRED_MSG)
    @Email(message = EMAIL_INVALID_MSG)
    private String email;

    public UserDTO() {}

    public UserDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
}
