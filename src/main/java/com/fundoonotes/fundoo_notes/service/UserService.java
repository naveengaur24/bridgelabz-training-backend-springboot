package com.fundoonotes.fundoo_notes.service;


import com.fundoonotes.fundoo_notes.dto.*;
import com.fundoonotes.fundoo_notes.exception.UserNotFoundException;

public interface UserService {

    ApiResponseDTO<UserResponseDTO> registerUser(RegisterRequestDTO request);

    ApiResponseDTO<AuthResponseDTO> loginUser(LoginRequestDTO request) throws UserNotFoundException;

    ApiResponseDTO<String> deleteUser(Long userId) throws UserNotFoundException;

    // Email bheja → OTP generate kia
    ApiResponseDTO<String> forgotPassword(ForgotPasswordRequestDTO request);

    //  OTP verify kia
    ApiResponseDTO<String> verifyOtp(VerifyOtpRequestDTO request);

    //  Naya password set kiya
    ApiResponseDTO<String> resetPassword(ResetPasswordRequestDTO request) throws UserNotFoundException;
}

//isme kya hai sbse phle user register krega postman se data aayega json format m jackson kya krega
//us data ko request.getEmail(), name() ye call krega or data hme milega object m
//
//registerUser - Year method  ka name hai..Controller direct isi ko call krega

//ApiResponseDto hmara return type hai ye hme return krta hai success, message, or
// UserResponse hme data provide krta hai..
