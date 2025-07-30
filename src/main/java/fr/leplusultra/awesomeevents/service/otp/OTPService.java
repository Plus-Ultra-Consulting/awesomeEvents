package fr.leplusultra.awesomeevents.service.otp;

import fr.leplusultra.awesomeevents.model.otp.OTP;
import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.repositories.otp.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class OTPService {
    private final OTPRepository otpRepository;
    @Value("${otp.expiration.time}")
    private int expirationTime;

    @Autowired
    public OTPService(OTPRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    public OTP findOtpByUserId(int userId) {
        return otpRepository.findByUserId(userId).orElse(null);
    }

    @Transactional
    public void invalidateCode(User user) {
        OTP otp = user.getOtp();
        if (otp != null) {
            user.setOtp(null);
            otpRepository.delete(otp);
        }
    }

    @Transactional
    public void createNewOneTimeCode(User user) {
        invalidateCode(user);

        OTP otp = new OTP();
        otp.setCode(generateOtp());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(expirationTime));
        otp.setUser(user);
        user.setOtp(otp);

        otpRepository.save(otp);
    }

    private String generateOtp() {
        String chars = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }
}
