package com.eventgate.userservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;
    private String fullName;

    private boolean isSeller = false;
    private boolean isAdmin = false;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime lastLogin;

    @ManyToMany
//    @JoinTable(
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "following_id")
//    )
    private Set<User> followings = new HashSet<>();

    @ManyToMany(mappedBy = "followings")
    private Set<User> followers = new HashSet<>();

    public void follow(User targetUser) {
        followings.add(targetUser);
        targetUser.getFollowers().add(this);
    }

    public void unfollow(User targetUser) {
        followings.remove(targetUser);
        targetUser.getFollowers().remove(this);
    }
}
