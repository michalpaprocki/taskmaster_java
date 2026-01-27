package com.mike.taskmaster.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mike.taskmaster.advice.GlobalExceptionHandler.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse error = new ErrorResponse(
                "JWT missing or invalid", 
                request.getRequestURI(),
                LocalDateTime.now().toString()
        );


        response.getOutputStream().println(objectMapper.writeValueAsString(error));
    }
}
