package com.eventgate.userservice.mappers;

import com.eventgate.userservice.dtos.UserResponse;
import com.eventgate.userservice.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail()
        );
    }
}
