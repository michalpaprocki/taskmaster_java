package com.mike.taskmaster.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import com.mike.taskmaster.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import com.mike.taskmaster.dto.UserRequestDTO;
import com.mike.taskmaster.dto.UserUpdateDTO;
import com.mike.taskmaster.entity.User;
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

        User user = new User(userDto.getName(), userDto.getEmail(), userDto.getPassword());
        return userRepository.save(user);
    }
    public User getUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found"));
        return user;
    }

    public String getEmail(UUID id) {
        User user = getUser(id);
        return user.getEmail();
    }

    public User updateUser(UserUpdateDTO dto) {
        User user = userRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (dto.getName() != null) 
            user.setName(dto.getName());
        if (dto.getEmail() != null)
            user.setEmail(dto.getEmail());
        if (dto.getPassword() != null)
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(user);
    }

    public String softDeleteUser(UserUpdateDTO dto) {
        User user = userRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
        return "User deleted successfully";
    }

    public String hardDeleteUser(UserUpdateDTO dto) {
        User user = userRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.delete(user);
        return "User deleted successfully";
    }
    
    

}
