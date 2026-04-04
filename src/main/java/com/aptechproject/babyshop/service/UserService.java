package com.aptechproject.babyshop.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aptechproject.babyshop.constant.AppConstants;
import com.aptechproject.babyshop.model.Cart;
import com.aptechproject.babyshop.model.User;
import com.aptechproject.babyshop.repository.CartRepository;
import com.aptechproject.babyshop.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
    }

    public User registerUser(User newUser) {
        // -- CREATE USER
        // 1. Find if they already exist
        // 2. If existing throw an error if not register the user, if not Step 3
        // 3. Hash the password and save User

        // USER NEEDS A CART
        // 4. Call dependency CartRepository and create new cart, attach to user.
        // 5. Save Cart 


        // 1
        Optional<User> existingUser = userRepository.findByEmail(newUser.getEmail());
        
        // 2
        if (existingUser.isPresent()) {
            throw new RuntimeException(AppConstants.ERROR_USER_ALREADY_EXISTS);
        }

        // 3
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User savedUser = userRepository.save(newUser);

        // 4
        Cart cart = new Cart();
        cart.setUser(savedUser);
        cartRepository.save(cart);

        return savedUser;
    }

    public User loginUser(String email, String rawPassword) {
        // 1. Find the user by email if not exists the throw and Error
        // 2. Check the password using passwordEncoder(s) match Method
        // 3. Return user (if success)

        // 1
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException(AppConstants.ERROR_INVALID_CREDENTIALS));
        // 2
        boolean isPasswordValid = passwordEncoder.matches(rawPassword, user.getPassword());
        

        if (!isPasswordValid) throw new RuntimeException(AppConstants.ERROR_INVALID_CREDENTIALS);
        return user;
    }
}
