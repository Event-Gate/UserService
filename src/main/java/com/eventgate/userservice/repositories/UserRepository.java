package com.eventgate.userservice.repositories;

import com.eventgate.userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.followings f WHERE f.id = :userId")
    Set<User> findFollowersById(@Param("userId") UUID userId);
    @Query("SELECT f FROM User u JOIN u.followings f WHERE u.id = :userId")
    Set<User> findFollowingsById(@Param("userId") UUID userId);
}
