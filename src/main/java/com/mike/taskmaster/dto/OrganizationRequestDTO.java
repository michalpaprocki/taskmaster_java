package com.mike.taskmaster.dto;

import java.util.List;
import java.util.UUID;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OrganizationRequestDTO {
    
    public static final String NAME_MIN_MSG = "Name must be at least 3 characters long";
    public static final String DESC_MIN_MSG = "Description must be at least 10 characters long";
    public static final String NAME_REQUIRED_MSG = "Name is required";

    public enum Action {
    ADD,
    REMOVE
    }

    @NotBlank(message = NAME_REQUIRED_MSG)
    @Size(min = 3, message = NAME_MIN_MSG)
    @NotNull
    private String name;

    @Size(min = 10, message = DESC_MIN_MSG)
    private String description;

    private List<UUID> members;

    private Action action;

    public OrganizationRequestDTO() {

    }
    public OrganizationRequestDTO(String name, String description, List<UUID> members, Action action) {
        this.name = name;
        this.description = description != null ? description : "";
        this.action = action;
        if (members != null) {
                this.members = members;
            }
    }

    public String getName() {
        return name;
    }
    public String getDescription(){
        return description;
    }
    public Action getAction() {
        return action;
    }
    public List<UUID> getMembers() {
        return members;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescrition(String description) {
        this.description = description;
    }
    public void setAction(Action action) {
        this.action = action;
    }
    public void setMembers(List<UUID> members) {
        this.members = members;
    }
}
