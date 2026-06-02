package com.fundoonotes.fundoo_notes.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}