package com.mike.taskmaster.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        return userRepository.save(UserMapper.toEntity(userDto, passwordEncoder));
    }
    public UserResponseDTO getUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found"));
        return UserMapper.toResponse(user);
    }
    public User getUserEntity(UUID id) {
        return userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found"));
    }
    public List<User> getUsersEntities(List<UUID> ids) {
        return userRepository.findAllById(ids);
    }
    public List<User> getUserEntities(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<User> users = userRepository.findAllById(ids);
        return users;
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
    
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return user;
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDTO> dtos = users.stream().map(user ->  UserMapper.toResponse(user)).collect(Collectors.toList());
        return dtos;
    }
}