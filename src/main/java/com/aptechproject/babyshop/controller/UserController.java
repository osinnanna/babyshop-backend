package com.aptechproject.babyshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptechproject.babyshop.constant.AppConstants;
import com.aptechproject.babyshop.model.User;
import com.aptechproject.babyshop.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(AppConstants.API_USERS)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(AppConstants.API_REGISTER)
    public ResponseEntity<?> registerUser(@Valid @RequestBody User newUser) {
        // using @Valid checks the acutal validations set in the User.java file

        // 1. Call UserService to register User
        // 2. Return upon success a response containing the user
        // 3. If user already exists return it and repond to said situation
        try {
            userService.registerUser(newUser);

            return ResponseEntity.status(HttpStatus.CREATED).body(newUser.getEmail());
        } catch (RuntimeException e) { // The error that was thrown in UserService.registerUser()
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
