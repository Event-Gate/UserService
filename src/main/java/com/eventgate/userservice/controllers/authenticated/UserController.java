package com.eventgate.userservice.controllers.authenticated;

import com.eventgate.userservice.dtos.DataUserRequest;
import com.eventgate.userservice.dtos.DataUserResponse;
import com.eventgate.userservice.entities.User;
import com.eventgate.userservice.exceptions.UnauthorizedException;
import com.eventgate.userservice.mappers.UserMapper;
import com.eventgate.userservice.services.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController @RequiredArgsConstructor @PreAuthorize("isAuthenticated()") @RequestMapping("/api/auth/customers")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PutMapping("/update")
    public ResponseEntity<DataUserResponse> updateCurrentCustomer(@Valid @RequestBody DataUserRequest request) throws UnauthorizedException {
        User updatedUser = userService.updateAuthenticatedCustomerDetails(request);
        DataUserResponse response = userMapper.toDataCustomerResponse(updatedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{sellerId}/follow")
    public ResponseEntity<?> follow(@PathVariable("sellerId") UUID sellerId) throws EntityNotFoundException, UnauthorizedException {
        userService.follow(sellerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{sellerId}/unfollow")
    public ResponseEntity<?> unfollow(@PathVariable("sellerId") UUID sellerId) throws EntityNotFoundException, UnauthorizedException {
        userService.unfollow(sellerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
