package com.mike.taskmaster.service;


import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mike.taskmaster.dto.OrganizationRequestDTO;
import com.mike.taskmaster.dto.OrganizationResponseDTO;
import com.mike.taskmaster.entity.Membership;
import com.mike.taskmaster.entity.Organization;
import com.mike.taskmaster.entity.User;
import com.mike.taskmaster.repository.OrganizationRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrganizationService {

    private OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public OrganizationResponseDTO createOrganizationWithOwner(OrganizationRequestDTO dto, User creator) {
        if (organizationRepository.existsByName(dto.getName())){
            throw new IllegalArgumentException("Organization name already taken");
        }
        
        Organization org = new Organization(dto.getName());

        Membership ownerMembership = new Membership();
        ownerMembership.setUser(creator);
        ownerMembership.setRole(Membership.Role.OWNER);
        org.addMembership(ownerMembership);
        organizationRepository.save(org);
        return new OrganizationResponseDTO(org);       
    }

    public OrganizationResponseDTO getOrganization(UUID id) {
        Organization org = organizationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Organization not found"));
        return new OrganizationResponseDTO(org);
    }

    public Organization getOrganizationEntity(UUID id) {
        Organization org = organizationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Organization not found"));
        return org;
    }


    public boolean isOwner(UUID id, User user) {
        Organization org = getOrganizationEntity(id);
        return org.getMemberships().stream().anyMatch(m -> m.getUser().equals(user) && m.getRole() == Membership.Role.OWNER);
    }

    public OrganizationResponseDTO addMember(UUID id, User member) {
        Organization org = getOrganizationEntity(id);
        Boolean exists = org.getMemberships().stream().anyMatch(m -> m.getUser().getId().equals(member.getId()));
        if (exists) {
            throw new IllegalArgumentException("User is already a member of this organization");
        }
        Membership membership = new Membership();
        membership.setUser(member);
        org.addMembership(membership);
        organizationRepository.save(org);
        OrganizationResponseDTO orgRespDto = new OrganizationResponseDTO(org);
        return orgRespDto;
    }

    public OrganizationResponseDTO getMembers(UUID id) {
        Organization org = getOrganizationEntity(id);
        OrganizationResponseDTO orgRespDto = new OrganizationResponseDTO(org);
        return orgRespDto;
    }

    public OrganizationResponseDTO removeMember(UUID id, User member) {
        Organization org = getOrganizationEntity(id);
        Optional<Membership> maybeMembership = org.getMemberships().stream().filter(m -> m.getUser().getId().equals(member.getId())).findFirst();
        if (maybeMembership.isPresent()) {
            Membership foundMember = maybeMembership.get();
            org.removeMembership(foundMember);
            organizationRepository.save(org);
            return new OrganizationResponseDTO(org);
        } else {
            throw new IllegalArgumentException("User is not a member of this organization");
        }
    }

    public OrganizationResponseDTO updateName(UUID id, OrganizationRequestDTO dto) {
        Organization org = getOrganizationEntity(id);
        if (organizationRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Organization name already taken");
        }

        org.setName(dto.getName());
        organizationRepository.save(org);
        return new OrganizationResponseDTO(org);
    }
}
