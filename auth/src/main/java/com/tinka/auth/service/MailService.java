package com.tinka.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String verificationUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Verify your email");

            // ✅ HTML email content
            String htmlContent = """
                <html>
                <body style="font-family: Arial, sans-serif; background-color: #f8f9fa; padding: 20px;">
                    <table width="100%%" cellpadding="0" cellspacing="0" style="max-width: 600px; margin: auto; background: white; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
                        <tr>
                            <td style="padding: 20px 30px;">
                                <h2 style="color: #333;">Welcome to <span style="color: #ffa200;">Tinka Marketplace</span> 👋</h2>
                                <p style="color: #555;">Hi there,</p>
                                <p style="color: #555;">Thank you for signing up. Please verify your email address by clicking the button below:</p>
                                <div style="margin: 20px 0; text-align: center;">
                                    <a href="%s" style="background-color: #ffa200; color: white; padding: 12px 24px; border-radius: 5px; text-decoration: none; font-weight: bold;">Verify Email</a>
                                </div>
                                <p style="color: #999; font-size: 12px;">If you did not create a Tinka account, please ignore this email.</p>
                                <p style="color: #999; font-size: 12px;">– The Tinka Team</p>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
            """.formatted(verificationUrl);

            // ✅ Plain text fallback + HTML email
            String plainText = "Please verify your email by visiting the following link: " + verificationUrl;

            helper.setText(plainText, htmlContent); // Plain text fallback + HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}
