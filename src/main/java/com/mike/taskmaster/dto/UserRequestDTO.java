package com.mike.taskmaster.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRequestDTO {
    
    public static final String NAME_MIN_MSG = "Name must be at least 3 characters long";
    public static final String NAME_REQUIRED_MSG = "Name is required";
    public static final String EMAIL_REQUIRED_MSG = "Email is required";
    public static final String EMAIL_INVALID_MSG = "Email must be valid";
    public static final String PASSWORD_REQUIRED_MSG = "Password is required";
    public static final String PASSWORD_INVALID_MSG = "Password must be at least 6 characters long and contain at least one lowercase and one uppercase letter";

    @NotBlank(message = NAME_REQUIRED_MSG)
    @Size(min = 3, message = NAME_MIN_MSG)
    private String name;

    @NotBlank(message = EMAIL_REQUIRED_MSG)
    @Email(message = EMAIL_INVALID_MSG)
    private String email;

    @NotBlank(message = PASSWORD_REQUIRED_MSG)
    @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z]).{6,}$",
    message = PASSWORD_INVALID_MSG
    )
    private String password;

    public UserRequestDTO() {}

    public UserRequestDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}

}
