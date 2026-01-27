package com.mike.taskmaster.controllers;



import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mike.taskmaster.dto.UserRequestDTO;
import com.mike.taskmaster.entity.RefreshToken;
import com.mike.taskmaster.entity.User;
import com.mike.taskmaster.security.jwt.JwtProperties;
import com.mike.taskmaster.security.jwt.JwtTokenProvider;
import com.mike.taskmaster.service.RefreshTokenService;
import com.mike.taskmaster.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtProperties jwtProperties;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider, JwtProperties jwtProperties, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtProperties = jwtProperties;
        this.refreshTokenService = refreshTokenService;
    }
    
    
    @Operation(summary = "Registers users and emits jwt token")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserRequestDTO dto, HttpServletResponse response) {

            User user = userService.createUser(dto);

            return respondWithTokens(response, user);
        }
  
    @Operation(summary = "Logs in users and returns jwt token")
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserRequestDTO dto, HttpServletResponse response) {
        User user = userService.login(dto.getEmail(), dto.getPassword());

        return respondWithTokens(response, user);
        }
  


    @Operation(summary = "Refreshes jwt tokens")
    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshJwt(HttpServletRequest request, HttpServletResponse response) {

        Optional<String> maybeRawRefreshToken = extractRefreshToken(request);
       
        if (maybeRawRefreshToken.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        String rawRefreshToken = maybeRawRefreshToken.get();


        RefreshToken refreshToken;
        try {
            refreshToken = refreshTokenService.validateRefreshToken(rawRefreshToken);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }

        return respondWithTokens(response, refreshToken.getUser());


    }
    @Operation(summary = "Logs user out")
    @PostMapping("/logout")
    public ResponseEntity<Map<String,String>> logout(HttpServletRequest request, HttpServletResponse response) {
         
        Optional<String> maybeRawRefreshToken = extractRefreshToken(request);
        maybeRawRefreshToken.ifPresent(rawRefreshToken -> {
            try {
                RefreshToken refreshToken = refreshTokenService.validateRefreshToken(rawRefreshToken);
 
                refreshTokenService.removeTokenByUser(refreshToken.getUser());
            } catch (Exception ignored) {
                // ignore if token invalid/expired
            }
        });

            ResponseCookie jwtCookie = clearCookies("accessToken");
            ResponseCookie refreshCookie = clearCookies("refreshToken");
            
            return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .build();
    }
    @Operation(summary = "Check if user session is valid")
    @GetMapping("/session")
    public ResponseEntity<Map<String, String>> checkSesion(HttpServletRequest request) {
        Optional<String> maybeRawRefreshToken = extractRefreshToken(request);

        if(maybeRawRefreshToken.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "No refresh token provided"));
        }

        try {
            refreshTokenService.validateRefreshToken(maybeRawRefreshToken.get());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Refresh token invalid"));
        }
    }

    @Transactional
    private ResponseEntity<Void> respondWithTokens(HttpServletResponse response, User user)  {
        refreshTokenService.removeTokenByUser(user);
        String newJwt = jwtTokenProvider.generateToken(user);
        String newRefreshTokenValue = refreshTokenService.createOrReplaceRefreshToken(user);
        ResponseCookie jwtCookie = ResponseCookie.from("accessToken", newJwt)
                .httpOnly(true)
                .secure(jwtProperties.isSecure())
                .sameSite("Lax")
                .path("/")
                .maxAge(jwtProperties.getExpiration() / 1000)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", newRefreshTokenValue)
                .httpOnly(true)
                .secure(jwtProperties.isSecure())
                .path("/")
                .maxAge(jwtProperties.getRefreshExpiration() / 1000)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();
    }
    private Optional<String> extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() !=null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                   return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }
    private ResponseCookie clearCookies(String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
            .httpOnly(true)
            .secure(jwtProperties.isSecure())
            .sameSite("Lax")
            .path("/")
            .maxAge(0)
            .build();
            return cookie;
    }
}