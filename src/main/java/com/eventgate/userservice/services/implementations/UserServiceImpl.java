package com.eventgate.userservice.services.implementations;

import com.eventgate.userservice.dtos.DataUserResponse;
import com.eventgate.userservice.mappers.UserMapper;
import com.eventgate.userservice.utils.SecurityUtils;
import com.eventgate.userservice.dtos.DataUserRequest;
import com.eventgate.userservice.entities.User;
import com.eventgate.userservice.exceptions.UnauthorizedException;
import com.eventgate.userservice.repositories.UserRepository;
import com.eventgate.userservice.services.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final UserMapper userMapper;

    @Override
    public User updateAuthenticatedCustomerDetails(DataUserRequest request) throws UnauthorizedException {
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
    public void follow(UUID targetUserId) throws EntityNotFoundException, UnauthorizedException {
        User authenticatedUser = securityUtils.getAuthenticatedUser();

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(EntityNotFoundException::new);

        if (targetUser.isSeller()) {
            authenticatedUser.follow(targetUser);
            userRepository.save(authenticatedUser);
        } else {
            throw new UnauthorizedException("You are not allowed to follow a normal user");
        }
    }

    @Override
    public void unfollow(UUID targetUserId) throws EntityNotFoundException, UnauthorizedException {
        User authenticatedUser = securityUtils.getAuthenticatedUser();

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(EntityNotFoundException::new);

        authenticatedUser.unfollow(targetUser);
        userRepository.save(authenticatedUser);
    }

    @Override
    public Set<String> getFollowers(UUID userId) throws EntityNotFoundException {
        return userRepository.findFollowersById(userId)
                .stream().map(User::getEmail).collect(Collectors.toSet());
    }

    @Override
    public Set<UUID> getFollowings(UUID userId) throws EntityNotFoundException {
        return userRepository.findFollowingsById(userId)
                .stream().map(User::getId).collect(Collectors.toSet());
    }

    @Override
    public DataUserResponse getUser(UUID userId) throws EntityNotFoundException {
        return userRepository.findById(userId)
                .map(userMapper::toDataCustomerResponse)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
    }
}