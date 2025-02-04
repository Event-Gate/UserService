package com.eventgate.userservice.services.interfaces;

import com.eventgate.userservice.dtos.UserRequest;
import com.eventgate.userservice.dtos.UserResponse;
import com.eventgate.userservice.dtos.UserResult;
import com.eventgate.userservice.entities.User;
import com.eventgate.userservice.exceptions.EntityNotFoundException;
import com.eventgate.userservice.exceptions.UnauthorizedException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Set;

public interface UserService {
    User updateAuthenticatedCustomerDetails(UserRequest request) throws UnauthorizedException, EntityNotFoundException;

    void follow(String userId) throws EntityNotFoundException, UnauthorizedException;
    void unfollow(String userId) throws EntityNotFoundException, UnauthorizedException;

    Set<String> getFollowers(String userId) throws EntityNotFoundException;
    Set<String> getFollowings(String userId) throws EntityNotFoundException;

    UserResult processOAuth2User(OAuth2User oAuth2User);
    UserResponse getUser(String userId) throws EntityNotFoundException;
    UserResponse getAuthenticatedUser() throws EntityNotFoundException, UnauthorizedException;
}
