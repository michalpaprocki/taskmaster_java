package com.mike.taskmaster.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mike.taskmaster.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    boolean existsByName(String name);
    Optional<User> findByEmail(String email);
}
