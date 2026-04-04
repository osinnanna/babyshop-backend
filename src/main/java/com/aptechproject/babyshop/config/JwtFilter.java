package com.aptechproject.babyshop.config;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aptechproject.babyshop.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Get the "Authorization" header using request.getHeader
        // 2. if no token let it go though
        // 3. get just the token by cutting of Bearer
        // 4. Use jwtSerive to read the token
        // 5. If valid create a VIP pass (Auth object)
        // 6. Tell Spring; this user is authenticated

        // 1
        String authHeader = request.getHeader("Authorization");

        // 2
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3
        String token = authHeader.substring((7));

        try {
            // 4
            String email = jwtService.extractEmail(token);
            String role = jwtService.extractRole(token);

            // 5
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Using the SecurityCTXHolder to let spring know that user is authenticated
                // wrap in SimpleGrantedAuthority
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null, Collections.singletonList(authority));

                // 6
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Any errors then do nothing.
        }

        filterChain.doFilter(request, response);
    }
    
}
