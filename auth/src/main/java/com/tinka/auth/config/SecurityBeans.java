// src/main/java/com/tinka/auth/config/SecurityBeans.java
package com.tinka.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeans {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Supports {bcrypt}, {pbkdf2}, etc. and prefixes hashes with the id.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // Or: return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
}
