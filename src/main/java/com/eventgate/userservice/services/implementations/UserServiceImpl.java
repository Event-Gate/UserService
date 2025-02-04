package com.eventgate.userservice.services.implementations;

import com.eventgate.userservice.dtos.UserResponse;
import com.eventgate.userservice.dtos.UserResult;
import com.eventgate.userservice.exceptions.EntityNotFoundException;
import com.eventgate.userservice.mappers.UserMapper;
import com.eventgate.userservice.utils.SecurityUtils;
import com.eventgate.userservice.dtos.UserRequest;
import com.eventgate.userservice.entities.User;
import com.eventgate.userservice.exceptions.UnauthorizedException;
import com.eventgate.userservice.repositories.UserRepository;
import com.eventgate.userservice.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final UserMapper userMapper;

    @Override
    public User updateAuthenticatedCustomerDetails(UserRequest request) throws UnauthorizedException, EntityNotFoundException {
        User user = securityUtils.getAuthenticatedUser();

        Optional.ofNullable(request.fullName())
                .filter(fullName -> !fullName.trim().isEmpty())
                .ifPresent(user::setFullName);

        Optional.ofNullable(request.phone())
                .filter(phone -> !phone.trim().isEmpty())
                .ifPresent(user::setPhone);

        return userRepository.save(user);
    }

    @Override
    public void follow(String targetUserId) throws EntityNotFoundException, UnauthorizedException {
        User authenticatedUser = securityUtils.getAuthenticatedUser();

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (targetUser.isSeller()) {
            authenticatedUser.getFollowingIds().add(targetUser.getId());
            userRepository.save(authenticatedUser);

            targetUser.getFollowerIds().add(authenticatedUser.getId());
            userRepository.save(targetUser);
        } else {
            throw new UnauthorizedException("You are not allowed to follow a normal user");
        }
    }

    @Override
    public void unfollow(String targetUserId) throws EntityNotFoundException, UnauthorizedException {
        User authenticatedUser = securityUtils.getAuthenticatedUser();

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        authenticatedUser.getFollowingIds().remove(targetUser.getId());
        userRepository.save(authenticatedUser);

        targetUser.getFollowerIds().remove(authenticatedUser.getId());
        userRepository.save(targetUser);
    }

    @Override
    public Set<String> getFollowers(String userId) {
        Set<String> followerIds = userRepository.findFollowerIdsById(userId);
        if (followerIds.isEmpty()) {
            return Collections.emptySet();
        }
        return userRepository.findAllById(followerIds).stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getFollowings(String userId) {
        Set<String> followingIds = userRepository.findFollowingIdsById(userId);
        if (followingIds.isEmpty()) {
            return Collections.emptySet();
        }
        return userRepository.findAllById(followingIds).stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());
    }

    @Override
    public UserResult processOAuth2User(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.getId() == null) {
                throw new IllegalStateException("User ID cannot be null for existing user");
            }
            return new UserResult(new UserResponse(user.getId(), user.getFullName(), user.getEmail()), false);
        }

        User newUser = createNewUser(oAuth2User);
        if (newUser.getId() == null) {
            throw new IllegalStateException("User ID cannot be null for new user");
        }

        return new UserResult(new UserResponse(newUser.getId(), newUser.getFullName(), newUser.getEmail()), true);
    }

    @Override
    public UserResponse getUser(String userId) throws EntityNotFoundException {
        return userRepository.findById(userId)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
    }

    @Override
    public UserResponse getAuthenticatedUser() throws EntityNotFoundException, UnauthorizedException {
        User user = securityUtils.getAuthenticatedUser();
        return userMapper.toResponse(user);
    }

    private User createNewUser(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String fullName = oAuth2User.getAttribute("given_name") + " " + oAuth2User.getAttribute("family_name");

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        return userRepository.save(newUser);
    }
}