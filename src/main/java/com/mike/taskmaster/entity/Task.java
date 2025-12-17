package com.mike.taskmaster.entity;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;


@Entity
@Table(name = "tasks")
public class Task {

    public enum Status {
    COMPLETED,
    IN_PROGESS,
    CANCELED,
    PENDING
    }

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = Status.PENDING;
        }
    }

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
        inverseJoinColumns = @JoinColumn(name = "user_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"task_id", "user_id"})
    )
    private Set<User> assignees = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;
    
    private LocalDateTime deadline;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public Task() {
        
    }
    public Task(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.creator = user;
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
    public Status getStatus() {
        return status;
    }
    public Set<User> getAssignees() {
        return assignees;
    }
    public LocalDateTime getDeadline() {
        return deadline;
    }
    public boolean getIsDeleted() {
        return isDeleted;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCreator(User creator) {
        this.creator = creator;
    }
    public void addAssignees(Set<User> assignees) {
        this.assignees.addAll(assignees);
    }
    public void removeAssignees(Set<User> assignees) {
        this.assignees.removeAll(assignees);
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
 }
