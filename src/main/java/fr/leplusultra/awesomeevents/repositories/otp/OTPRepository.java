package fr.leplusultra.awesomeevents.repositories.otp;

import fr.leplusultra.awesomeevents.model.otp.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP, Integer> {
    Optional<OTP> findByUserId(int userId);
}
