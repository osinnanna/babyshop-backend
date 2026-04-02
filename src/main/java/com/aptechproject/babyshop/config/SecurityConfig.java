package com.aptechproject.babyshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
 /*
    The moment you add the dependency **implementation 'org.springframework.boot:spring-boot-starter-security'**, it acts like a hyper-aggressive nightclub bouncer. By default, it completely locks down every single URL in your app and demands a username and password just to look at the site. This would break the /register endpoint you just built! need for a configuration file to tell the bouncer: "Keep the doors unlocked for registration and login, but let us use your BCrypt tool."
 */

    @Bean // 1. create bcrypt tool and make avaialable to the rest of the application
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Configure the bouncers rules (we dont want on register and login)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/**").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
