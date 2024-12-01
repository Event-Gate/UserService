package com.eventgate.userservice.services.implementations;

import com.eventgate.userservice.entities.Customer;
import com.eventgate.userservice.repositories.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service @RequiredArgsConstructor
public class CustomerDetailsServiceImpl implements UserDetailsService {
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Customer customer = customerRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            if (customer.isAdmin()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            if (customer.isSeller()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
            }

            return new User(
                    customer.getEmail(),
                    "",
                    authorities
            );
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error occurred while fetching user: " + email, e);
        }
    }
}