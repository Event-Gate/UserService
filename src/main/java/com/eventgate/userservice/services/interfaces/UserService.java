package com.eventgate.userservice.services.interfaces;

import com.eventgate.userservice.dtos.DataUserRequest;
import com.eventgate.userservice.entities.User;
import com.eventgate.userservice.exceptions.UnauthorizedException;
import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;

public interface UserService {
    User updateAuthenticatedCustomerDetails(DataUserRequest request) throws UnauthorizedException;

    void follow(UUID userId) throws EntityNotFoundException, UnauthorizedException;
    void unfollow(UUID userId) throws EntityNotFoundException, UnauthorizedException;
}
