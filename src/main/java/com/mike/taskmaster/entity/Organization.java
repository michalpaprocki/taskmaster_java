package com.mike.taskmaster.entity;


import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
// maybe implement soft delete
@Entity
@Table(name = "organizations")
public class Organization {
    

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, updatable = true)
    private LocalDateTime updatedAt;

    public Organization() {
        
    }
    public Organization(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    } 
    public void addMembership(Membership membership) {
        memberships.add(membership);
        membership.setOrganization(this);
    }
    public void removeMembership(Membership membership) {
        memberships.remove(membership);
        membership.setOrganization(this);
    }
    public List<Membership> getMemberships() {
        return memberships;
    }
    public List<Membership> getOwners() {
        return memberships.stream().filter(m -> m.getRole() == Membership.Role.OWNER).toList();
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescrition(String description) {
        this.description = description;
    }

}
