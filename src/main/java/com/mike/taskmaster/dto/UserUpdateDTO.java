package com.mike.taskmaster.dto;

import java.util.UUID;

import com.mike.taskmaster.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import static com.mike.taskmaster.dto.UserRequestDTO.EMAIL_INVALID_MSG;
import static com.mike.taskmaster.dto.UserRequestDTO.PASSWORD_INVALID_MSG;

public class UserUpdateDTO {
    private UUID id;
    private String name;

    @Email(message = EMAIL_INVALID_MSG)
    private String email;


    @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z]).{6,}$",
    message = PASSWORD_INVALID_MSG
    )
    private String password;

    public UserUpdateDTO() {}

    public UserUpdateDTO(UUID id) {
        this.id = id;
    }

    public UserUpdateDTO(UUID id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public UserUpdateDTO(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
    public UUID getId() {return id;}
    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    
    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
