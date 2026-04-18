package com.aptechproject.babyshop.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptechproject.babyshop.constant.AppConstants;
import com.aptechproject.babyshop.dto.LoginRequest;
import com.aptechproject.babyshop.model.ErrorMessage;
import com.aptechproject.babyshop.model.User;
import com.aptechproject.babyshop.service.JwtService;
import com.aptechproject.babyshop.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(AppConstants.API_USERS)
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping(AppConstants.API_REGISTER)
    public ResponseEntity<?> registerUser(@Valid @RequestBody User newUser) {
        // using @Valid checks the acutal validations set in the User.java file

        // 1. Call UserService to register User
        // 2. Return upon success a response containing the user
        // 3. If user already exists return it and repond to said situation
        try {
            userService.registerUser(newUser);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", newUser.getEmail()));
        } catch (RuntimeException e) { // The error that was thrown in UserService.registerUser()
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(e.getMessage()));
        }
    }

    @PostMapping(AppConstants.API_LOGIN)
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        // 1. Using try catch to implement the already made method in UserService
        // 2. In try use the email and password in LoginRequest to login
        // 3. If success generate a token and respond with token
        // 4. In catch, the thrown RuntimeException should be handled properly

        // 1
        try {
            // 2
            User loginUser = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

            // 3
            String token = jwtService.generateToken(loginUser);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessage(e.getMessage()));
        }
    }

    @GetMapping(AppConstants.API_ME)
    public ResponseEntity<?> getCurrentUser() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.getCurrentUser(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessage(e.getMessage()));
        }
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String resetLink = userService.generatePasswordResetToken(request.get("email"));
            return ResponseEntity.ok(Map.of("message", "Check your email", "resetLink", resetLink));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            userService.resetPassword(request.get("token"), request.get("newPassword"));
            return ResponseEntity.ok(Map.of("message", "Password successfully updated!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }
}
