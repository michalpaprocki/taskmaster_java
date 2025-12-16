package com.mike.taskmaster.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class OrganizationRequestDTO {
    
    public static final String NAME_MIN_MSG = "Name must be at least 3 characters long";
    public static final String NAME_REQUIRED_MSG = "Name is required";

    public enum Action {
    ADD,
    REMOVE
    }

    @NotBlank(message = NAME_REQUIRED_MSG)
    @Size(min = 3, message = NAME_MIN_MSG)
    private String name;

    private Action action;

    public OrganizationRequestDTO() {

    }
    public OrganizationRequestDTO(String name, Action action) {
        this.name = name;
        this.action = action;
    }

    public String getName() {
        return name;
    }
    public Action getAction() {
        return action;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAction(Action action) {
        this.action = action;
    }
}
