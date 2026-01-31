package com.mike.taskmaster.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import static com.mike.taskmaster.dto.UserRequestDTO.PASSWORD_INVALID_MSG;
import static com.mike.taskmaster.dto.UserRequestDTO.PASSWORD_REQUIRED_MSG;

public class ChangePasswordRequestDTO {
    @NotBlank
    private String oldPassword;

    @NotBlank(message = PASSWORD_REQUIRED_MSG)
    @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z]).{6,}$",
    message = PASSWORD_INVALID_MSG
    )
    private String newPassword;

    public ChangePasswordRequestDTO() {}
    
    public ChangePasswordRequestDTO(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    public String getOldPassword() {
        return oldPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }
}
