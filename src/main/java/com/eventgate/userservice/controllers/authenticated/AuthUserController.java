package com.eventgate.userservice.controllers.authenticated;

import com.eventgate.userservice.dtos.UserRequest;
import com.eventgate.userservice.dtos.UserResponse;
import com.eventgate.userservice.entities.User;
import com.eventgate.userservice.exceptions.EntityNotFoundException;
import com.eventgate.userservice.exceptions.UnauthorizedException;
import com.eventgate.userservice.mappers.UserMapper;
import com.eventgate.userservice.services.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController @RequiredArgsConstructor @RequestMapping("/api/auth/users")
public class AuthUserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ResponseEntity<>("Logged out successfully.", HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateCurrentCustomer(@Valid @RequestBody UserRequest request) throws UnauthorizedException, EntityNotFoundException {
        User updatedUser = userService.updateAuthenticatedCustomerDetails(request);
        UserResponse response = userMapper.toResponse(updatedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{sellerId}/follow")
    public ResponseEntity<?> follow(@PathVariable("sellerId") String sellerId) throws EntityNotFoundException, UnauthorizedException {
        userService.follow(sellerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{sellerId}/unfollow")
    public ResponseEntity<?> unfollow(@PathVariable("sellerId") String sellerId) throws EntityNotFoundException, UnauthorizedException {
        userService.unfollow(sellerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getAuthenticatedUser() throws EntityNotFoundException, UnauthorizedException {
        UserResponse user = userService.getAuthenticatedUser();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
