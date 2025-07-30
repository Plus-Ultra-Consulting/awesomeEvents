package fr.leplusultra.awesomeevents.service.email;

import com.google.zxing.WriterException;
import fr.leplusultra.awesomeevents.model.event.Event;
import fr.leplusultra.awesomeevents.model.person.Person;
import fr.leplusultra.awesomeevents.model.user.User;
import fr.leplusultra.awesomeevents.service.qrcode.QRCodeService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Transactional(readOnly = true)
public class EmailService {
    private final JavaMailSender mailSender;
    private final QRCodeService qrCodeService;
    @Value("${mail.sender.address}")
    private String emailSenderAddress;
    @Value("${qrcode.image.format}")
    private String imageFormat;
    @Value("${frontend.url}")
    private String frontEndURL;


    @Autowired
    public EmailService(JavaMailSender mailSender, QRCodeService qrCodeService) {
        this.mailSender = mailSender;
        this.qrCodeService = qrCodeService;
    }

    public void sendEmailWithSecurityCodeToPerson(Person person) throws MessagingException, WriterException, IOException {
        Event event = person.getEvent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        messageHelper.setFrom(emailSenderAddress);
        messageHelper.setTo(person.getEmail());
        messageHelper.setSubject("Invitation to " + event.getName());

        InputStreamSource imageSource = convertBufferedImageToInputStreamSource(
                qrCodeService.convertToImage(qrCodeService.generateBasicQRCode(frontEndURL + "/event/"
                        + person.getEvent().getId() + "/securityCode/" + person.getSecurityCode())));

        String cid = "qr-code";

        String htmlMsg = "<h3>You were invited to attend " + event.getName() +
                "<br>Place: " + event.getPlace() +
                "<br>Start time: " + event.getStartAt() +
                "<br>Your security code is: <b>" + person.getSecurityCode() +
                "</b><br>" +
                "Or you can scan this QR code:<br>" +
                "<img src='cid:" + cid + "'/></h3>";

        messageHelper.setText(htmlMsg, true);

        messageHelper.addInline(cid, imageSource, "image/" + imageFormat);

        mailSender.send(message);
    }

    public void sendEmailWithOTPToUser(User user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(emailSenderAddress);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject("One-Time Code to log in");

        String htmlMsg = "<h3>Your one-time code to log in is: <b> " + user.getOtp().getCode() + "</b></h3>";

        messageHelper.setText(htmlMsg, true);

        mailSender.send(message);
    }

    private InputStreamSource convertBufferedImageToInputStreamSource(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, imageFormat, baos);
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        baos.close();
        return new ByteArrayResource(imageBytes);
    }
}
