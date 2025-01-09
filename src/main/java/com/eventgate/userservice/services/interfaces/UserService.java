package com.eventgate.userservice.services.interfaces;

import com.eventgate.userservice.dtos.DataUserRequest;
import com.eventgate.userservice.dtos.DataUserResponse;
import com.eventgate.userservice.entities.User;
import com.eventgate.userservice.exceptions.UnauthorizedException;
import jakarta.persistence.EntityNotFoundException;

import java.util.Set;
import java.util.UUID;

public interface UserService {
    User updateAuthenticatedCustomerDetails(DataUserRequest request) throws UnauthorizedException;

    void follow(UUID userId) throws EntityNotFoundException, UnauthorizedException;
    void unfollow(UUID userId) throws EntityNotFoundException, UnauthorizedException;

    Set<String> getFollowers(UUID userId) throws EntityNotFoundException;
    Set<UUID> getFollowings(UUID userId) throws EntityNotFoundException;

    DataUserResponse getUser(UUID userId) throws EntityNotFoundException;
}
