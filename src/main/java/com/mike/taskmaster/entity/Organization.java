package com.mike.taskmaster.entity;


import java.util.UUID;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "organizations")
public class Organization {
    
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "organizations", fetch = FetchType.LAZY)
    private List<User> users;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

}
