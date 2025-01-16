package com.eventgate.userservice.mappers;

import com.eventgate.userservice.dtos.DataUserResponse;
import com.eventgate.userservice.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public DataUserResponse toResponse(User user) {
        return new DataUserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail()
        );
    }
}
