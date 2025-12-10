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

    private String name;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships = new ArrayList<>();


    protected Organization() {
        
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
}
