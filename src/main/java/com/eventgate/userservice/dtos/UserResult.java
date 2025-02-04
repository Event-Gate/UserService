package com.eventgate.userservice.dtos;

public record UserResult(
        UserResponse userResponse,
        boolean isNewUser
) {}
