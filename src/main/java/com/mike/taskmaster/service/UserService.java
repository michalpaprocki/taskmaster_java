package com.mike.taskmaster.service;

import org.springframework.stereotype.Service;

import java.util.UUID;
import com.mike.taskmaster.repository.UserRepository;
import com.mike.taskmaster.dto.UserDTO;
import com.mike.taskmaster.entity.User;
@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(UserDTO userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }

        User user = new User(userDto.getName(), userDto.getEmail());
        return userRepository.save(user);
    }
    public User getUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("User not found"));
        return user;
    }

    public String getEmail(UUID id) {
        User user = getUser(id);
        return user.getEmail();
    }
}
