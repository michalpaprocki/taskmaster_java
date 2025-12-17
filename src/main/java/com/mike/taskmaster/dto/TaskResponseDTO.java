package com.mike.taskmaster.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mike.taskmaster.entity.Task;
import com.mike.taskmaster.entity.User;
import com.mike.taskmaster.entity.Task.Status;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private User creator;
    private Set<User> assignees;
    private Status status;
    private LocalDateTime deadline;
    private boolean isDeleted;

    public TaskResponseDTO(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.creator = task.getCreator();
        this.assignees = task.getAssignees();
        this.deadline = task.getDeadline();
        this.isDeleted = task.getIsDeleted();
        this.status = task.getStatus();
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
    public void setAssignees(Set<User> assignees) {
        this.assignees = assignees;
    }
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public UUID getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public UserResponseDTO getCreator() {
        return new UserResponseDTO(creator);
    }
    public Set<UserResponseDTO> getAssignees() {
        return assignees.stream().map(a -> new UserResponseDTO(a)).collect(Collectors.toSet());
    }
    public LocalDateTime getDeadline() {
        return deadline;
    }
    public boolean getIsDeleted() {
        return isDeleted;
    }
    public Status getStatus() {
        return status;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof UserResponseDTO)) return false;
        UserResponseDTO other = (UserResponseDTO) o;
        return Objects.equals(this.id, other.getId());
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
}
}
