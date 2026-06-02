package com.fundoonotes.fundoo_notes.controller;

import com.fundoonotes.fundoo_notes.dto.*;
import com.fundoonotes.fundoo_notes.exception.UserNotFoundException;
import com.fundoonotes.fundoo_notes.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.fundoonotes.fundoo_notes.dto.ForgotPasswordRequestDTO;
import com.fundoonotes.fundoo_notes.dto.ResetPasswordRequestDTO;
import com.fundoonotes.fundoo_notes.dto.VerifyOtpRequestDTO;
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    // Token se userId nikaalane ka helper method
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }

    // POST /api/users/register
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> registerUser(
            @Valid @RequestBody RegisterRequestDTO request) {
        ApiResponseDTO<UserResponseDTO> response = userService.registerUser(request);
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // POST /api/users/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> loginUser(
            @RequestBody LoginRequestDTO request) throws UserNotFoundException {
        ApiResponseDTO<AuthResponseDTO> response = userService.loginUser(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // DELETE /api/users
    // Token se userId lenge — URL mein nahi denge!
    @DeleteMapping
    public ResponseEntity<ApiResponseDTO<String>> deleteUser() {
        Long userId = getCurrentUserId();
        log.info("Delete user request for userId: {}", userId);
        ApiResponseDTO<String> response = userService.deleteUser(userId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    //  Email bhejenge -> OTP aayega
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponseDTO<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO request) {
        ApiResponseDTO<String> response = userService.forgotPassword(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    // OTP verify kiya..
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponseDTO<String>> verifyOtp(@Valid @RequestBody VerifyOtpRequestDTO request) {
        ApiResponseDTO<String> response = userService.verifyOtp(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //  Naya password set krenge
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDTO<String>> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        ApiResponseDTO<String> response = userService.resetPassword(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}