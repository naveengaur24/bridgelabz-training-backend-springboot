package com.fundoonotes.fundoo_notes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequestDTO {

    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Invalid email format!")
    private String email;
}