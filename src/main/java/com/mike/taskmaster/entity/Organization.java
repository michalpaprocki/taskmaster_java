package com.mike.taskmaster.entity;


import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "organizations")
public class Organization {
    

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships = new ArrayList<>();


    protected Organization() {
        
    }
    public Organization(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
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

}
