package com.eventgate.userservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String email;
    private String fullName;
    @Column(unique = true)
    private String phone;
    private String recoveryEmail;
    private boolean isSeller = false;
    private boolean isAdmin = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime lastLogin;
}
