package com.fundoonotes.fundoo_notes.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kis email ke liye OTP hai
    @Column(nullable = false)
    private String email;

    // 6 digit OTP
    @Column(nullable = false)
    private String otp;

    // OTP kab expire hoga
    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;

    // OTP use hua ya nahi
    @Column(name = "is_used")
    private boolean isUsed = false;

    // Kab create hua
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        // OTP 10 minutes mein expire hoga
        this.expiryTime = LocalDateTime.now().plusMinutes(10);
    }
}