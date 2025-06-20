package fr.leplusultra.awesomeevents.service.email;

import fr.leplusultra.awesomeevents.model.event.Event;
import fr.leplusultra.awesomeevents.model.person.Person;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${mail.sender.address}")
    private String emailSenderAddress;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmailWithSecurityCode(Person person) throws MessagingException {
        Event event = person.getEvent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(emailSenderAddress);
        messageHelper.setTo(person.getEmail());
        messageHelper.setSubject("Invitation to " + event.getName());

        String htmlMsg = "<h3>You were invited to attend" + event.getName() +
                "<br>Place: " + event.getPlace() +
                "<br>Start time: " + event.getStartAt() +
                "<br>Your security code is: <b>" + person.getSecurityCode() +
                "</b></h3>";
        messageHelper.setText(htmlMsg, true);

        mailSender.send(message);
    }
}
