package com.mike.taskmaster.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mike.taskmaster.entity.Task;

public interface TaskRepository extends JpaRepository<Task, UUID>{

}
