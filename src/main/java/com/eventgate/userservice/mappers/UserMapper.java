package com.eventgate.userservice.mappers;

import com.eventgate.userservice.dtos.DataUserResponse;
import com.eventgate.userservice.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public DataUserResponse toDataCustomerResponse(User user) {
        if (user == null) {
            return null;
        }

        return new DataUserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail()
        );
    }
}
