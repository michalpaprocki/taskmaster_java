package com.mike.taskmaster.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;


import org.springframework.stereotype.Service;

import com.mike.taskmaster.entity.RefreshToken;
import com.mike.taskmaster.entity.User;
import com.mike.taskmaster.exception.InvalidTokenException;
import com.mike.taskmaster.repository.RefreshTokenRepository;
import com.mike.taskmaster.security.jwt.JwtProperties;

import jakarta.transaction.Transactional;
@Transactional
@Service
public class RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtProperties jwtProperties;
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtProperties jwtProperties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtProperties = jwtProperties;
    }

    public String createOrReplaceRefreshToken(User user) {
        RefreshToken token = refreshTokenRepository.findByUser(user)
            .orElseGet(RefreshToken::new);

        
        token.setUser(user); // needed if new
        token.setTokenHash(hashToken(UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString()));
        token.setExpiration(LocalDateTime.now().plus(Duration.ofMillis(jwtProperties.getRefreshExpiration())));
        
            refreshTokenRepository.saveAndFlush(token);
        return token.getTokenHash();
    }

    public Optional<RefreshToken> findByRawToken(String rawToken) {
        String hashed = hashToken(rawToken);
        return refreshTokenRepository.findByTokenHash(hashed);
    }
    public Optional<RefreshToken> findByUser(User user) {
        return refreshTokenRepository.findByUser(user);
    }
    public void removeTokenByUser(User user) {
            Optional<RefreshToken> maybeToken = findByUser(user);
            maybeToken.ifPresent(refreshTokenRepository::delete);
            refreshTokenRepository.flush();
        
    }   

    public RefreshToken removeRefreshToken(String rawToken) {
    return findByRawToken(rawToken)
        .map(refreshToken -> {
            refreshTokenRepository.delete(refreshToken);
            refreshTokenRepository.flush();
            return refreshToken;
        })
        .orElseThrow(() -> new RuntimeException("Refresh token not found"));
    }


    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(rawToken.getBytes((StandardCharsets.UTF_8)));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing token", e);
        }
    }
    @Transactional
    public RefreshToken validateRefreshToken(String rawToken) {

    return refreshTokenRepository.findByTokenHash(rawToken)
        .orElseThrow(() -> new InvalidTokenException("Invalid token"));
}
}
