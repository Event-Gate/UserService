package com.eventgate.userservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Document
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    private String id;
    private String email;
    private String phone;
    private String fullName;

    private boolean isSeller = false;
    private boolean isAdmin = false;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime lastLogin;
    private Set<String> followingIds = new HashSet<>();
    private Set<String> followerIds = new HashSet<>();
}
