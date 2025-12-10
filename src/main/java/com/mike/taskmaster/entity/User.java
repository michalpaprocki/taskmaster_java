package com.mike.taskmaster.entity;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;



@Entity
@Table(name = "users")
public class User {

    public enum Role {
    USER,
    ADMIN
}

    @PrePersist
    public void prePersist() {
        if (role == null) {
            role = Role.USER;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships = new ArrayList<>();
 

    @ManyToMany(mappedBy = "assignees", cascade = CascadeType.PERSIST)
    private List<Task> tasks;

    private boolean isDeleted;
    private LocalDateTime deletedAt;

    protected User(){}

    public User(String name, String email, String password){
        this.email = email;
        this.name = name;
        this.password = password;
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
    

    public List<Task> getTasks() {
        return tasks;
    }

    public Role getRole() {
        return role;
    }
    
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void addMembership(Membership membership) {
        memberships.add(membership);
        membership.setUser(this);
    }

    public List<Membership> getMemberships() {
        return memberships;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
