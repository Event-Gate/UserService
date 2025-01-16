package com.eventgate.userservice.dtos;

public record DataUserResponse(
        String id,
        String fullName,
        String email
) {}