package com.mike.taskmaster.dto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.mike.taskmaster.entity.Organization;

public class OrganizationResponseDTO {
    private UUID id;
    private String name;
    private List<MemberResponseDTO> members;
    
    public OrganizationResponseDTO(){}
    public OrganizationResponseDTO(Organization org){
        this.id = org.getId();
        this.name = org.getName();
        this.members = org.getMemberships().stream().map(MemberResponseDTO::new).collect(Collectors.toList());
    }

    public String getName() {return name;}
    public UUID getId() {return id;}
    public List<MemberResponseDTO> getMemberships() {return members;}

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setMemberships(List<MemberResponseDTO> memberships) {
        this.members = memberships;
    }

}
