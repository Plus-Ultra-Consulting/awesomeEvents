package fr.leplusultra.awesomeevents.repositories.otp;

import fr.leplusultra.awesomeevents.model.otp.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Integer> {
    Optional<OTP> findByUserId(int userId);
}
