package com.mike.taskmaster.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="refresh_tokens")
public class RefreshToken {
    

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @Column(name="expires_at", nullable = false)
    private LocalDateTime expiresAt;  

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public RefreshToken() {}

    public UUID getId() {return id;}
    public String getTokenHash() {return tokenHash;}
    public LocalDateTime getExpiresAt() {return expiresAt;}
    public User getUser() {return user;}
    public void setId(UUID id) {
        this.id = id;
    }
    public void setTokenHash(String hash) {
        this.tokenHash = hash;
    }
    public void setExpiration(LocalDateTime date) {
        this.expiresAt = date;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
