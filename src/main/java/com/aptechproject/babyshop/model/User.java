package com.aptechproject.babyshop.model;

import com.aptechproject.babyshop.constant.AppConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity 
@Table(name = "users") 
public class User {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @NotBlank(message = AppConstants.VALIDATION_NAME_EMPTY)
    private String name;

    @Email(message = AppConstants.VALIDATION_EMAIL_INVALID) 
    @NotBlank(message = AppConstants.VALIDATION_EMAIL_EMPTY)
    @Column(unique = true) 
    private String email;

    @JsonIgnore
    @NotBlank(message = AppConstants.VALIDATION_PASSWORD_EMPTY)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;
}