package com.mike.taskmaster.security.jwt;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mike.taskmaster.dto.UserResponseDTO;
import com.mike.taskmaster.mapper.UserMapper;
import com.mike.taskmaster.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {


        if ("OPTIONS".equalsIgnoreCase(request.getMethod())){
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractToken(request);
        
        if(token != null) {
            try {
                UUID id = jwtTokenProvider.parseTokenForUUID(token);
                UserResponseDTO dto = userService.getUser(id);
                UserDetails userDetails = UserMapper.fromUserResponseDTO(dto);

                Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                    SecurityContextHolder.clearContext();
    logger.warn("JWT validation failed: " + e.getMessage());
                // Invalid JWT → do nothing, Spring will reject the request

            }

            }
            filterChain.doFilter(request, response);
    }

    public String extractToken(HttpServletRequest request) {
    if(request.getCookies() != null) {
        for (Cookie cookie : request.getCookies()) {
            if("accessToken".equals(cookie.getName())){
                return cookie.getValue();
            }
        }
    }
    String bearer = request.getHeader("Authorization");
    if(bearer != null && bearer.startsWith("Bearer ")) {
        return bearer.substring(7);
    }
    return null;
}
}
