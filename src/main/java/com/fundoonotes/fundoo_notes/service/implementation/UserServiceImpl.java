package com.fundoonotes.fundoo_notes.service.implementation;
import com.fundoonotes.fundoo_notes.dto.*;
import com.fundoonotes.fundoo_notes.exception.UserNotFoundException;
import com.fundoonotes.fundoo_notes.model.User;
import com.fundoonotes.fundoo_notes.repository.UserRepository;
import com.fundoonotes.fundoo_notes.service.EmailService;
import com.fundoonotes.fundoo_notes.service.RedisService;
import com.fundoonotes.fundoo_notes.service.UserService;
import com.fundoonotes.fundoo_notes.service.EmailProducer;
import com.fundoonotes.fundoo_notes.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private EmailProducer emailProducer;

    @Override
    public ApiResponseDTO<UserResponseDTO> registerUser(RegisterRequestDTO request) {
        log.info("Register request aaya: {}", request.getEmail());   // {}-> its a placeholder..
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Email already exists: {}", request.getEmail());
            return new ApiResponseDTO<>(false, "Email already registered", null);
        }

        // MANUAL MAPPING — DTO → Entity
//        User user = new User();
//        user.setName(request.getName());
//        user.setEmail(request.getEmail());
//        user.setPassword(request.getPassword());

        // MODEL MAPPER — DTO → Entity (Ek Line m...)
        User user = modelMapper.map(request, User.class);

        User savedUser = userRepository.save(user);
        log.info("User saved with id: {}", savedUser.getId());

        UserResponseDTO responseDTO = convertToResponseDTO(savedUser);
        return new ApiResponseDTO<>(true, "User registered successfully", responseDTO);
    }

    @Override
    public ApiResponseDTO<AuthResponseDTO> loginUser(LoginRequestDTO request) throws UserNotFoundException {
        log.info("Login request came : {}", request.getEmail());

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            log.warn("User not found : {}", request.getEmail());
            throw new UserNotFoundException("User not found!");
        }
        User user = userOptional.get();
        if (!user.getPassword().equals(request.getPassword())) {
            log.warn("Wrong password for: {}", request.getEmail());
            return new ApiResponseDTO<>(false, "Invalid email or password!", null);
        }

        // TOKEN GENERATE kia..
        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        log.info("Token generated for: {}", user.getEmail());

        // AuthResponseDTO banaya — token ke saath
        AuthResponseDTO authResponse = new AuthResponseDTO(
                token,
                user.getId(),
                user.getEmail(),
                user.getName()
        );
        return new ApiResponseDTO<>(true, "Login successful!", authResponse);
    }
    @Override
    public ApiResponseDTO<String> deleteUser(Long userId) throws UserNotFoundException {
        log.info("Deleting user with id: {}", userId);

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found!");
        }

        userRepository.delete(userOptional.get());
        log.info("User deleted with id: {}", userId);
        return new ApiResponseDTO<>(true, "User deleted successfully!", null);
    }

    // forgot pass.
    @Override
    public ApiResponseDTO<String> forgotPassword(ForgotPasswordRequestDTO request) {
        log.info("Forgot password request: {}", request.getEmail());

        // Email exist karta hai
        if (!userRepository.existsByEmail(request.getEmail())) {
            return new ApiResponseDTO<>(false, "Email not registered!", null);
        }

        // 6 digit OTP generate kiya
        String otp = String.format("%06d", new Random().nextInt(999999));
        log.info("OTP generated for: {}", request.getEmail());

        // OTP Redis mein save kiya — 10 minute expiry
        String redisKey = "otp:" + request.getEmail();
        redisService.setWithExpiry(redisKey, otp, 10, TimeUnit.MINUTES);  //timeunit is a enum (fixed data type)  it gave time in minute..
        log.info("OTP stored in Redis for: {}", request.getEmail());

        // PEHLE:
        // emailService.sendOtpEmail(request.getEmail(), otp);
        // AB — RabbitMQ Queue mein message daalenge..!
        emailProducer.sendEmailMessage(
                request.getEmail(),
                "Fundoo Notes - Password Reset OTP",
                "Hello!\n\n" +
                        "Your OTP for password reset is: " + otp + "\n\n" +
                        "This OTP is valid for 10 minutes only.\n\n" +
                        "Regards,\nFundoo Notes Team"
        );
        log.info("Email message added to queue for: {}", request.getEmail());
        return new ApiResponseDTO<>(true, "OTP sent to your email!", null);
    }

    // verify otp..
    @Override
    public ApiResponseDTO<String> verifyOtp(VerifyOtpRequestDTO request) {
        log.info("OTP verify request: {}", request.getEmail());

        // Redis se OTP fetch karenge..
        String redisKey = "otp:" + request.getEmail();
        Object storedOtp = redisService.get(redisKey);

        // OTP exist karta hai? (null = expired ya galat email)
        if (storedOtp == null) {
            return new ApiResponseDTO<>(false, "OTP expired or invalid!", null);
        }

        // OTP match karo
        if (!storedOtp.toString().equals(request.getOtp())) {
            return new ApiResponseDTO<>(false, "Invalid OTP!", null);
        }
        log.info("OTP verified for: {}", request.getEmail());
        // OTP valid! Redis se delete karo — reuse nahi ho sake!
//        redisService.delete(redisKey);
//        log.info("OTP verified and deleted from Redis for: {}", request.getEmail());

        return new ApiResponseDTO<>(true, "OTP verified successfully!", null);
    }

    // password reset..
    @Override
    public ApiResponseDTO<String> resetPassword(ResetPasswordRequestDTO request) throws UserNotFoundException {
        log.info("Reset password request: {}", request.getEmail());

        // Redis se OTP verify krenge
        String redisKey = "otp:" + request.getEmail();
        Object storedOtp = redisService.get(redisKey);

        // OTP exist karta hai?
        if (storedOtp == null) {
            return new ApiResponseDTO<>(false, "OTP expired or invalid!", null);
        }

        // OTP match krenge..
        if (!storedOtp.toString().equals(request.getOtp())) {
            return new ApiResponseDTO<>(false, "Invalid OTP!", null);
        }

        // OTP valid! Redis se delete kiya..
        redisService.delete(redisKey);
        log.info("OTP used and deleted from Redis for: {}", request.getEmail());

        // User findout kia
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found!");
        }

        // Password update karo
        User user = userOptional.get();
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        log.info("Password reset successful: {}", request.getEmail());

        return new ApiResponseDTO<>(true, "Password reset successfully!", null);
    }

    private UserResponseDTO convertToResponseDTO(User user) {

        // MANUAL MAPPING — Entity → ResponseDTO
//        return new UserResponseDTO(
//                user.getId(),
//                user.getName(),
//                user.getEmail(),
//                user.getCreatedAt()
//        );

        // MODEL MAPPER — Entity → ResponseDTO (Ek Line!)
        return modelMapper.map(user, UserResponseDTO.class);
    }
}