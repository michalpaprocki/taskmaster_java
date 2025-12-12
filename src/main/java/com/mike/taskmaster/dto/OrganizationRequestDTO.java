package com.mike.taskmaster.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class OrganizationRequestDTO {
    
    public static final String NAME_MIN_MSG = "Name must be at least 3 characters long";
    public static final String NAME_REQUIRED_MSG = "Name is required";

    @NotBlank(message = NAME_REQUIRED_MSG)
    @Size(min = 3, message = NAME_MIN_MSG)
    private String name;

    public OrganizationRequestDTO() {

    }
    public OrganizationRequestDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
