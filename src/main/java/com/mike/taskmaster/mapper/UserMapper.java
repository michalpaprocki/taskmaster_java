package com.mike.taskmaster.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.mike.taskmaster.dto.UserRequestDTO;
import com.mike.taskmaster.dto.UserResponseDTO;
import com.mike.taskmaster.entity.User;

public class UserMapper {

    private UserMapper() {}

    public static UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getIsDeleted()
        );
    }

    public static User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setIsDeleted(dto.getIsDeleted() != null ? dto.getIsDeleted() : false);
        return user;
    }

    public static void updateEntity(User user, UserRequestDTO dto, PasswordEncoder passwordEncoder) {
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getIsDeleted() != null) {
            user.setIsDeleted(dto.getIsDeleted());
        }
    }
}
