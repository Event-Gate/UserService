package com.eventgate.userservice.services.interfaces;

import com.eventgate.userservice.dtos.DataUserRequest;
import com.eventgate.userservice.dtos.DataUserResponse;
import com.eventgate.userservice.entities.User;
import com.eventgate.userservice.exceptions.EntityNotFoundException;
import com.eventgate.userservice.exceptions.UnauthorizedException;

import java.util.Set;

public interface UserService {
    User updateAuthenticatedCustomerDetails(DataUserRequest request) throws UnauthorizedException, EntityNotFoundException;

    void follow(String userId) throws EntityNotFoundException, UnauthorizedException;
    void unfollow(String userId) throws EntityNotFoundException, UnauthorizedException;

    Set<String> getFollowers(String userId) throws EntityNotFoundException;
    Set<String> getFollowings(String userId) throws EntityNotFoundException;

    DataUserResponse getUser(String userId) throws EntityNotFoundException;
    DataUserResponse getAuthenticatedUser() throws EntityNotFoundException, UnauthorizedException;
}
