package com.mike.taskmaster.entity;

import java.util.UUID;
import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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

    protected User(){}

    public User(String name, String email){
        this.email = email;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    
    public List<Organization> getOrganizations() {
        return organizations;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
