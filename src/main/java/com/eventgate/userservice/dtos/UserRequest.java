package com.eventgate.userservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequest(
   @NotBlank(message = "Full name cannot be blank")
   @Size(min = 1, max = 30, message = "Full name must be between 1 and 30 characters")
   String fullName,
   @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format. Must start with + and contain 1-14 digits")
   String phone
) {}
