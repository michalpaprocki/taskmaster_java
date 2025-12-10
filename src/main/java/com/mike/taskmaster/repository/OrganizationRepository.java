package com.mike.taskmaster.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mike.taskmaster.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, UUID>{
    
}
