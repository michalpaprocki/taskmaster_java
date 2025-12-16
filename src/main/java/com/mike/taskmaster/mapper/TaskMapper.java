package com.mike.taskmaster.mapper;

import java.util.Collections;
import java.util.Set;

import com.mike.taskmaster.dto.TaskRequestDTO;
import com.mike.taskmaster.dto.TaskResponseDTO;
import com.mike.taskmaster.entity.Task;
import com.mike.taskmaster.entity.User;

public class TaskMapper {
    
    private TaskMapper() {}

    public static TaskResponseDTO toResponse(Task task) {
        return new TaskResponseDTO(task);
    }

    public static Task toEntity(TaskRequestDTO dto, User creator, Set<User> assignees) {
        Task task = new Task();
        task.setDescription(dto.getDescription());
        task.setTitle(dto.getTitle());
        task.setCreator(creator);
        task.setDeadline(dto.getDeadline());
        task.setStatus(dto.getStatus());
        task.setIsDeleted(dto.getIsDeleted());
        Set<User> safeAssignees = assignees != null ? assignees : Collections.emptySet();
        task.addAssignees(safeAssignees);
        return task;
    }

    public static void updateEntity(Task task, TaskRequestDTO dto, User creator, Set<User> assignees) {
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
        if (dto.getStatus() != null) {
            task.setStatus(dto.getStatus());

        }
        if (dto.getDeadline() != null) {
            task.setDeadline(dto.getDeadline());
        }
           
        task.setIsDeleted(dto.getIsDeleted());
        
    }
}
