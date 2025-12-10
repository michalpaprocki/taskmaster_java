package com.mike.taskmaster.entity;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToMany
    @JoinTable(
        name = "user_tasks",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> assignees = new ArrayList<>();

        
    protected Task() {
        
    }
    public UUID getId() {
        return  id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getCreator() {
        return creator;
    }

    public List<User> getAssignees() {
        return assignees;
    }
 }
