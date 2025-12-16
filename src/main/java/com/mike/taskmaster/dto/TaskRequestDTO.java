package com.mike.taskmaster.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import com.mike.taskmaster.entity.Task.Status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
    
public class TaskRequestDTO {
    public static final String TITLE_MIN_MSG = "Title must be at least 3 characters long";
    public static final String TITLE_REQUIRED_MSG = "Title is required";
    public static final String DESCRIPTION_MIN_MSG = "Description must be at least 12 characters long";
    public static final String DESCRIPTION_REQUIRED_MSG = "Description is required";

    public enum Action {
        ADD,
        REMOVE
    }

    @NotBlank(message = TITLE_REQUIRED_MSG)
    @Size(min = 3, message = TITLE_MIN_MSG)
    private String title;

    @NotBlank(message = DESCRIPTION_REQUIRED_MSG)
    @Size(min = 12, message = DESCRIPTION_MIN_MSG)
    private String description;

    private Set<UUID> assignees;

    private Action DTOAction;

    private boolean isDeleted = false;

    private Status status;

    private LocalDateTime deadline;

    public TaskRequestDTO(String title, String description, Set<UUID> assignees, TaskRequestDTO.Action action, Boolean isDeleted, Status status, LocalDateTime deadline) {
        this.title = title;
        this.description = description;
        this.assignees = assignees != null ? assignees : Collections.emptySet();
        this.isDeleted = isDeleted != null ? isDeleted : false;
        if (status != null) {
            this.status = status;
        }
        if (deadline != null) {
            this.deadline = deadline;
        }
        this.DTOAction = action;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setAssignees(Set<UUID> assignees) {
        this.assignees = assignees;
    }
    public void setDTOAction(Action action){
        this.DTOAction = action;
    }
    public void setIsDeleted(Boolean isDeleted) {
        if (isDeleted != null) this.isDeleted = isDeleted;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public void setDeadline(LocalDateTime deadline) {
       if(deadline != null) this.deadline = deadline;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public Set<UUID> getAssignees() {
        return assignees;
    }
    public Action getDTOAction() {
        return DTOAction;
    }
    public boolean getIsDeleted() {
        return isDeleted;
    }
    public Status getStatus() {
        return status;
    }
    public LocalDateTime getDeadline() {
        return deadline;
    }
}
