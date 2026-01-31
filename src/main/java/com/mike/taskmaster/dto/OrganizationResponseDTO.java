package com.mike.taskmaster.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


import com.mike.taskmaster.entity.Organization;

public class OrganizationResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private List<MemberResponseDTO> members;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public OrganizationResponseDTO(){}
    public OrganizationResponseDTO(Organization org){
        this.id = org.getId();
        this.name = org.getName();
        this.description = org.getDescription();
        this.members = org.getMemberships().stream().map(MemberResponseDTO::new).collect(Collectors.toList());
        this.createdAt = org.getCreatedAt();
        this.updatedAt = org.getUpdatedAt();
    }

    public String getName() {return name;}
    public UUID getId() {return id;}
    public String getDescription() {return description;}
    public List<MemberResponseDTO> getMemberships() {return members;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDescrition(String description) {
        this.description = description;
    }
    public void setMemberships(List<MemberResponseDTO> memberships) {
        this.members = memberships;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
