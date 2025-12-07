package com.mike.taskmaster.service;

import org.springframework.stereotype.Service;

import com.mike.taskmaster.repository.UserRepository;
import com.mike.taskmaster.entity.User;
@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(String name, String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        } 
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already taken");
        }

        User user = new User(name, email);
        return userRepository.save(user);
    }

}
