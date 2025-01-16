package com.eventgate.userservice.utils;

import com.eventgate.userservice.entities.User;
import com.eventgate.userservice.exceptions.EntityNotFoundException;
import com.eventgate.userservice.exceptions.UnauthorizedException;
import com.eventgate.userservice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component @AllArgsConstructor
public class SecurityUtils {
    private final UserRepository userRepository;

    public User getAuthenticatedUser() throws EntityNotFoundException, UnauthorizedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UnauthorizedException("No authentication found");
        }

        String email = null;
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User) {
            email = ((OAuth2User) principal).getAttribute("email");
        } else if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        }

        if (email == null) {
            throw new UnauthorizedException("Unable to extract email from authentication");
        }

        Optional<User> customer = userRepository.findByEmail(email);
        if (customer.isPresent()) {
            return customer.get();
        }
        throw new EntityNotFoundException("No customer found with email: " + email);
    }
}
