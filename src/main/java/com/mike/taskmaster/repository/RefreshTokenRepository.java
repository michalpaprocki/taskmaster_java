package com.mike.taskmaster.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mike.taskmaster.entity.RefreshToken;
import com.mike.taskmaster.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}

