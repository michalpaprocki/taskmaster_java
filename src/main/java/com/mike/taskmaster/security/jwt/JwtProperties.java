package com.mike.taskmaster.security.jwt;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtProperties {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    @Value("${jwt.cookie.secure}")
    private boolean secure;

    public boolean isSecure() {
    return secure;
    }
    public SecretKey getSecret() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }


    public long getExpiration() {
        return expiration;
    }
    public long getRefreshExpiration() {
        return refreshExpiration;
    }
}
