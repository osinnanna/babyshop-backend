package com.aptechproject.babyshop.service;

import org.springframework.stereotype.Service;

import com.aptechproject.babyshop.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    // Expire after 24 Hours
    private final long jwtExpirationDate = 86400000;

    // Generate the token
    public String generateToken(User user) {
        return Jwts.builder()
            .setSubject(user.getEmail())
            .claim("role", user.getRole().name())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationDate))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
