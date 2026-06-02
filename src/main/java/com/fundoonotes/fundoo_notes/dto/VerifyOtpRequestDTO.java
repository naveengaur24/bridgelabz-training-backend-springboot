package com.fundoonotes.fundoo_notes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyOtpRequestDTO {

    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Invalid email format!")  // ← ADD KIYA
    private String email;

    @NotBlank(message = "OTP cannot be empty!")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits!")
    private String otp;
}