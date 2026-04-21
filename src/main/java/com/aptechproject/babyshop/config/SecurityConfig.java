package com.aptechproject.babyshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aptechproject.babyshop.model.Role;

@Configuration
public class SecurityConfig {

    @Bean // 1. create bcrypt tool and make avaialable to the rest of the application
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Configure the bouncers rules (we dont want on register and login)
    // I -- RBAC --
    // 1. Only Admin can add product so check for admin role
    // 2. Anyone can view products/inventory
    // 3. add the scanner
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
          .csrf(csrf -> csrf.disable())
          // 1. Add this: Make the session stateless (vital for JWTs)
          .sessionManagement(session -> session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
          // 2. Add these: Disable the default "human" login methods
          .httpBasic(basic -> basic.disable())
          .formLogin(form -> form.disable())

          .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/products/add").hasAuthority(Role.ROLE_ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/products/*/ratings").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
