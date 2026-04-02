package com.aptechproject.babyshop.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aptechproject.babyshop.model.User;
import com.aptechproject.babyshop.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    } // Dependency injection

    public User registerUser(User newUser) {
        // 1. Find if they already exist
        // 2. If existing throw an error if not register the user, if not Step 3
        // 3. Hash the password and then save

        // 1
        Optional<User> existingUser = userRepository.findByEmail(newUser.getEmail());
        
        // 2
        if (existingUser.isPresent()) {
            throw new RuntimeException("You are already registered");
        }

        // 3
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        userRepository.save(newUser);
        return newUser;
    }

    public User loginUser(String email, String rawPassword) {
        // 1. Find the user by email if not exists the throw and Error
        // 2. Check the password using passwordEncoder(s) match Method
        // 3. Return user (if success)

        // 1
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid email or password"));
        // 2
        boolean isPasswordValid = passwordEncoder.matches(rawPassword, user.getPassword());
        

        if (!isPasswordValid) throw new RuntimeException("Invalid email or password");
        return user;
    }
}
