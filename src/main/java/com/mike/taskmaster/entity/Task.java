package com.mike.taskmaster.entity;

import java.util.UUID;
import java.util.List;

import jakarta.persistence.*;;


@Entity
@Table(name = "tasks")
public class Task {
    
    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private User master;

    @ManyToMany
    @JoinTable(
        name = "user_tasks",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> assignees;
}
