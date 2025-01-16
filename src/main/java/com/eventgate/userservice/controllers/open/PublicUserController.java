package com.eventgate.userservice.controllers.open;

import com.eventgate.userservice.dtos.DataUserResponse;
import com.eventgate.userservice.exceptions.EntityNotFoundException;
import com.eventgate.userservice.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
@RestController @RequiredArgsConstructor @RequestMapping("/api/public/users")
public class PublicUserController {
    private final UserService userService;

    @GetMapping("/{userId}/followings")
    public ResponseEntity<Set<String>> getFollowings(@PathVariable("userId") String userId) throws EntityNotFoundException {
        return new ResponseEntity<>(userService.getFollowings(userId), HttpStatus.OK);
    }

    @GetMapping("/{sellerId}/followers")
    public ResponseEntity<Set<String>> getFollowers(@PathVariable("sellerId") String sellerId) throws EntityNotFoundException {
        return new ResponseEntity<>(userService.getFollowers(sellerId), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<DataUserResponse> getUser(@PathVariable("userId") String userId) throws EntityNotFoundException {
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }
}