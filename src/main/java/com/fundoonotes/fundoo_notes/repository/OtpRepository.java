package com.fundoonotes.fundoo_notes.repository;

import com.fundoonotes.fundoo_notes.model.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpToken, Long> {

    // Email se latest OTP dhundhega
    Optional<OtpToken> findTopByEmailOrderByCreatedAtDesc(String email);

    // Email aur OTP se dhundho
    Optional<OtpToken> findByEmailAndOtpAndIsUsedFalse(String email, String otp);
}