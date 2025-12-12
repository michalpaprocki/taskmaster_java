package com.mike.taskmaster.dto;

import java.util.UUID;

import com.mike.taskmaster.entity.Organization;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.mike.taskmaster.dto.OrganizationRequestDTO.NAME_REQUIRED_MSG;
import static com.mike.taskmaster.dto.OrganizationRequestDTO.NAME_MIN_MSG;;

public class OrganizationUpdateDTO {
    private UUID id;

    @NotBlank(message = NAME_REQUIRED_MSG)
    @Size(min = 3, message = NAME_MIN_MSG)
    private String name;

    public OrganizationUpdateDTO() {

    }
    public OrganizationUpdateDTO(UUID id) {
        this.id = id;
    }
    public OrganizationUpdateDTO(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
     public OrganizationUpdateDTO(Organization org) {
        this.id = org.getId();
        this.name = org.getName();
    }
    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
}
