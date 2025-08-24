package com.tinka.auth.config;

import jakarta.mail.internet.MimeMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.*;
import java.util.Arrays;
import java.util.Properties;

@Configuration
@Profile("!prod") // Active in dev, test, but NOT in prod
public class DummyMailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSender() {
            @Override
            public MimeMessage createMimeMessage() {
                return new MimeMessage(Session.getDefaultInstance(new Properties()));
            }

            @Override
            public MimeMessage createMimeMessage(java.io.InputStream contentStream) {
                try {
                    return new MimeMessage(Session.getDefaultInstance(new Properties()), contentStream);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create dummy MimeMessage", e);
                }
            }

            @Override
            public void send(MimeMessage mimeMessage) {
                try {
                    System.out.println("🟡 Dummy HTML email sent to: " + Arrays.toString(mimeMessage.getAllRecipients()));
                    System.out.println("Subject: " + mimeMessage.getSubject());
                    System.out.println("Body: (HTML omitted for brevity)");
                } catch (Exception e) {
                    System.err.println("Failed to simulate sending email: " + e.getMessage());
                }
            }

            @Override
            public void send(MimeMessage... mimeMessages) {
                for (MimeMessage msg : mimeMessages) {
                    send(msg);
                }
            }

            @Override
            public void send(SimpleMailMessage simpleMessage) {
                System.out.println("🟡 Dummy plain email sent to: " + Arrays.toString(simpleMessage.getTo()));
                System.out.println("Subject: " + simpleMessage.getSubject());
                System.out.println("Text: " + simpleMessage.getText());
            }

            @Override
            public void send(SimpleMailMessage... simpleMessages) {
                for (SimpleMailMessage msg : simpleMessages) {
                    send(msg);
                }
            }
        };
    }
}

