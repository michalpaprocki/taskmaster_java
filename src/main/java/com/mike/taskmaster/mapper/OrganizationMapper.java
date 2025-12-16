package com.mike.taskmaster.mapper;

import java.util.Set;

import com.mike.taskmaster.dto.OrganizationRequestDTO;
import com.mike.taskmaster.dto.OrganizationResponseDTO;
import com.mike.taskmaster.entity.Membership;
import com.mike.taskmaster.entity.Organization;
import com.mike.taskmaster.entity.User;

public class OrganizationMapper {
    
    private OrganizationMapper() {}

    public static OrganizationResponseDTO toResponse(Organization organization) {
        return new OrganizationResponseDTO(organization);
    }

    public static Organization toEntity(OrganizationRequestDTO dto, User owner, Set<User> members) {
        Organization org = new Organization();
        org.setName(dto.getName());
        Membership ownerMembership = new Membership();
        ownerMembership.setUser(owner);
        ownerMembership.setRole(Membership.Role.OWNER);
        ownerMembership.setOrganization(org);
        org.addMembership(ownerMembership);

        if (members != null) {
            for (User m : members) {
                Membership memberMembership = new Membership();
                memberMembership.setUser(m);
                memberMembership.setRole(Membership.Role.MEMBER);
                memberMembership.setOrganization(org);
                org.addMembership(ownerMembership);
            }
        }
        return org;
    }
}
