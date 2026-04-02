package com.aptechproject.babyshop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) // GenerationType.IDENTITY is for Auto increment 1,2,3
    private Long id;

    @NotBlank(message = "Name cannot be empty") // Self explanatory
    private String name;

    @Email(message = "Must be a valid email format") // Validating email types
    @NotBlank(message = "Email cannot be empty")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
