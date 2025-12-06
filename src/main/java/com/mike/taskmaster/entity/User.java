package com.mike.taskmaster.entity;

import java.util.UUID;
import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(unique = true)
    private String email;

    @ManyToMany
    @JoinTable(
        name = "user_organizations",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "organization_id")    
    )
    private List<Organization> organizations;

    @ManyToMany(mappedBy = "assignees", cascade = CascadeType.PERSIST)
    private List<Task> tasks;
}
