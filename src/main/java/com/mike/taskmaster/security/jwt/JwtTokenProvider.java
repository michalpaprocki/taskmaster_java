package com.mike.taskmaster.security.jwt;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.mike.taskmaster.entity.User;
import com.mike.taskmaster.exception.JwtValidationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    } 

    public String generateToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getExpiration());
        
        // 🏆 located bad getter call that exploded the whole security filter (claims.getId()) || deleted custom claims and moved user data to subject - now everything just works 🏆 
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(jwtProperties.getSecret())
                .compact();
    }

    public UUID parseTokenForUUID(String token) {
         try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getSecret())
                .build()
                .parseClaimsJws(token)
                .getBody();


                UUID id = UUID.fromString(claims.getSubject());
        return id;
        } catch (Exception e) {
            throw new JwtValidationException("Invalid JWT token", e);
        } 
    }
    public boolean validateToken(String token) {
    try {
        Jwts.parserBuilder()
            .setSigningKey(jwtProperties.getSecret())
            .build()
            .parseClaimsJws(token);  // just parses to validate
        return true;
    } catch (Exception e) {
        return false;
    }
}
}
