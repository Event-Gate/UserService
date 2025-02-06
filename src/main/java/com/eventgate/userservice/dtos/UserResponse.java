package com.eventgate.userservice.dtos;

public record UserResponse(
        String id,
        String fullName,
        String email,
        boolean isSeller
) {}