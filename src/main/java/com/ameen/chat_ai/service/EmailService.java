package com.ameen.chat_ai.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * OTP reset mail – HTML format
     */
    public void sendOtpMail(String toEmail, String userName, String otp) throws MessagingException {
        String html = """
            <html>
              <body>
                <h3>Dear %s,</h3>
                <p>You requested a password reset. Use the OTP below to reset your password:</p>
                <h2 style='color:#4CAF50;'>%s</h2>
                <p>This OTP is valid for 3 minutes.</p>
                <p>If you did not request this, please ignore this email.</p>
                <br><p>Thanks,<br/>Orbit_AI Team</p>
              </body>
            </html>
            """.formatted(userName, otp);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("your-email@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject("Password Reset OTP");
        helper.setText(html, true);
        mailSender.send(message);
    }

}
