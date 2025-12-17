package com.mike.taskmaster.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.mike.taskmaster.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import com.mike.taskmaster.dto.UserRequestDTO;
import com.mike.taskmaster.dto.UserResponseDTO;
import com.mike.taskmaster.entity.User;
import com.mike.taskmaster.mapper.UserMapper;
@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User createUser(UserRequestDTO userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }
        if (userRepository.existsByName(userDto.getName())) {
            throw new IllegalArgumentException("Name already taken");
        }
        return userRepository.save(UserMapper.toEntity(userDto));
    }
    public UserResponseDTO getUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found"));
        return UserMapper.toResponse(user);
    }
    public User getUserEntity(UUID id) {
        return userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found"));
    }
    public Set<User> getUserEntities(Set<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptySet();
        }
        return new HashSet<>(userRepository.findAllById(ids));
    }
    public String getEmail(UUID id) {
        return getUser(id).getEmail();
    }

    public User updateUser(UUID id, UserRequestDTO dto) {
        User user = getUserEntity(id);
        UserMapper.updateEntity(user, dto, passwordEncoder);
        return userRepository.save(user);
    }

    public String softDeleteUser(UUID id) {
        User user = getUserEntity(id);
        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
        return "User deleted successfully";
    }

    public String hardDeleteUser(UUID id) {
        User user = getUserEntity(id);
        userRepository.delete(user);
        return "User deleted successfully";
    }
    
    

}
