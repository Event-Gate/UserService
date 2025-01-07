package com.eventgate.userservice.services.implementations;

import com.eventgate.userservice.entities.User;
import com.eventgate.userservice.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service @RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            if (user.isAdmin()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            if (user.isSeller()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    "",
                    authorities
            );
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error occurred while fetching user: " + email, e);
        }
    }
}