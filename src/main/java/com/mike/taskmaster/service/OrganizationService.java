package com.mike.taskmaster.service;


import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mike.taskmaster.dto.OrganizationRequestDTO;
import com.mike.taskmaster.dto.OrganizationResponseDTO;
import com.mike.taskmaster.dto.OrganizationUpdateDTO;
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

    public Organization createOrganizationWithOwner(OrganizationRequestDTO dto, User creator) {
        if (organizationRepository.existsByName(dto.getName())){
            throw new IllegalArgumentException("Organization name already taken");
        }
        
        Organization org = new Organization(dto.getName());

        Membership ownerMembership = new Membership();
        ownerMembership.setUser(creator);
        ownerMembership.setRole(Membership.Role.OWNER);
        org.addMembership(ownerMembership);

        return organizationRepository.save(org);       
    }

    public Organization getOrganizationInternal(OrganizationUpdateDTO dto) {
        Organization org = organizationRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("Organization not found"));
        return org;
    }

    public OrganizationResponseDTO getOrganizationExternal(OrganizationUpdateDTO dto) {
        Organization org = organizationRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("Organization not found"));
        return new OrganizationResponseDTO(org);
    }


    public boolean isOwner(OrganizationUpdateDTO orgDto, User user) {
        Organization org = getOrganizationInternal(orgDto);
        return org.getMemberships().stream().anyMatch(m -> m.getUser().equals(user) && m.getRole() == Membership.Role.OWNER);
    }

    public OrganizationResponseDTO addMember(OrganizationUpdateDTO dto, User member) {
        Organization org = organizationRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("Organization not found"));
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

    public OrganizationResponseDTO getMembers(OrganizationUpdateDTO dto) {
        Organization org = organizationRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("Organization not found"));
        OrganizationResponseDTO orgRespDto = new OrganizationResponseDTO(org);
        return orgRespDto;
    }

    public Boolean removeMember(OrganizationUpdateDTO dto, User member) {
        Organization org = organizationRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("Organization not found"));
        Optional<Membership> maybeMembership = org.getMemberships().stream().filter(m -> m.getUser().getId().equals(member.getId())).findFirst();
        if (maybeMembership.isPresent()) {
            Membership foundMember = maybeMembership.get();
            org.removeMembership(foundMember);
            organizationRepository.save(org);
            return true;
        } else {
            return false;
        }
    }

    public OrganizationResponseDTO updateName(OrganizationUpdateDTO dto, String name) {
        Organization org = organizationRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("Organization not found"));
        if (organizationRepository.existsByName(name)) {
            throw new IllegalArgumentException("Organization name already taken");
        }

        org.setName(name);
        organizationRepository.save(org);
        return new OrganizationResponseDTO(org);
    }
}
