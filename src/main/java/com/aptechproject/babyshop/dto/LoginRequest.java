package com.aptechproject.babyshop.dto;

import com.aptechproject.babyshop.constant.AppConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = AppConstants.VALIDATION_EMAIL_EMPTY)
    @Email(message = AppConstants.VALIDATION_EMAIL_INVALID)
    private String email;

    @NotBlank(message = AppConstants.VALIDATION_PASSWORD_EMPTY)
    private String password;
}