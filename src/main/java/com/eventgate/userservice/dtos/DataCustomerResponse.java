package com.eventgate.userservice.dtos;

import java.util.UUID;

public record DataCustomerResponse(
        UUID id,
        String fullName,
        String email
) {}