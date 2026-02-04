package com.mike.taskmaster.mapper;

import java.util.Collections;
import java.util.List;


import com.mike.taskmaster.dto.TaskRequestDTO;
import com.mike.taskmaster.dto.TaskResponseDTO;
import com.mike.taskmaster.entity.Organization;
import com.mike.taskmaster.entity.Task;
import com.mike.taskmaster.entity.User;

public class TaskMapper {
    
    private TaskMapper() {}

    public static TaskResponseDTO toResponse(Task task) {
        return new TaskResponseDTO(task);
    }

    public static Task toEntity(TaskRequestDTO dto, User creator, List<User> assignees, List<Organization> assignedOrganizations) {
        Task task = new Task();
        task.setDescription(dto.getDescription());
        task.setTitle(dto.getTitle());
        task.setCreator(creator);
        task.setDeadline(dto.getDeadline());
        task.setStatus(dto.getStatus());
        task.setIsDeleted(dto.getIsDeleted());
        List<Organization> safeOrganizations = assignedOrganizations != null ? assignedOrganizations : Collections.emptyList();
        task.addAssignedOrganizations(safeOrganizations);
        List<User> safeAssignees = assignees != null ? assignees : Collections.emptyList();
        task.addAssignees(safeAssignees);
        return task;
    }

    public static Task updateEntity(Task task, TaskRequestDTO dto, User creator, List<User> assignees, List<Organization> assignedOrganizations) {
        if (dto.getDescription() != null) {
            task.setDescription(dto.getDescription());
        }
        if (dto.getTitle() != null) {
            task.setTitle(dto.getTitle());
        }
        if (assignees != null && dto.getDTOAction() != null) {
            switch (dto.getDTOAction()) {
                case ADD:
                    task.getAssignees().addAll(assignees);
                break;

                case REMOVE:
                    task.getAssignees().removeAll(assignees);
                break;
            }
        }
        if (assignedOrganizations != null && dto.getDTOAction() != null) {
            switch (dto.getDTOAction()) {
                case ADD:
                    task.getAssignedOrganizations().addAll(assignedOrganizations);
                break;

                case REMOVE:
                    task.getAssignedOrganizations().removeAll(assignedOrganizations);
                break;
            }
        }
        if (dto.getStatus() != null) {
            task.setStatus(dto.getStatus());

        }
        if (dto.getDeadline() != null) {
            task.setDeadline(dto.getDeadline());
        }
           
        task.setIsDeleted(dto.getIsDeleted());
        
        return task;
    }
}
