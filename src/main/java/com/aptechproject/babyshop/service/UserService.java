package com.aptechproject.babyshop.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aptechproject.babyshop.constant.AppConstants;
import com.aptechproject.babyshop.model.Cart;
import com.aptechproject.babyshop.model.PasswordResetToken;
import com.aptechproject.babyshop.model.User;
import com.aptechproject.babyshop.repository.CartRepository;
import com.aptechproject.babyshop.repository.PasswordResetTokenRepository;
import com.aptechproject.babyshop.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final PasswordResetTokenRepository tokenRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CartRepository cartRepository, PasswordResetTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public User registerUser(User newUser) {
        // -- CREATE USER
        // 1. Find if they already exist
        // 2. If existing throw an error if not register the user, if not Step 3
        // 3. Hash the password and save User

        // USER NEEDS A CART
        // 4. Call dependency CartRepository and create new cart, attach to user.
        // 5. Save Cart 


        // 1 & 2
        userRepository.findByEmail(newUser.getEmail()).ifPresent(user -> {
            throw new RuntimeException(AppConstants.ERROR_USER_ALREADY_EXISTS);
        });
        
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

    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException(AppConstants.ERROR_INVALID_CREDENTIALS));
    }

    @Transactional
    public String generatePasswordResetToken(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException(AppConstants.ERROR_INVALID_CREDENTIALS));

        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);

        String token = java.util.UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDateTime(LocalDateTime.now().plusMinutes(15));

        tokenRepository.save(resetToken);

        return "babyshop://reset-password?token=" + token;
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException(AppConstants.ERROR_INVALID_TOKEN));

        // Check if its expired
        if (resetToken.getExpiryDateTime().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException(AppConstants.ERROR_INVALID_TOKEN);
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Delete used token
        tokenRepository.delete(resetToken);
    }


}