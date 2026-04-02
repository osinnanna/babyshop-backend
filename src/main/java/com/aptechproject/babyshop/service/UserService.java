package com.aptechproject.babyshop.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aptechproject.babyshop.model.User;
import com.aptechproject.babyshop.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    } // Dependency injection

    public User registerUser(User newUser) {
        // 1. Find if they already exist
        // 2. If existing throw an error if not register the user, if not save

        // 1
        Optional<User> existingUser = userRepository.findByEmail(newUser.getEmail());
        
        // 2
        if (existingUser.isPresent()) {
            throw new RuntimeException("You are already registered");
        }

        userRepository.save(newUser);
        return newUser;
    }
}
