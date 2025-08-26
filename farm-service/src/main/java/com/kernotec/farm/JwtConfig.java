package com.kernotec.farm;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        String secret = "only-test-secret-key";
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

        return NimbusJwtDecoder.withSecretKey(secretKey)
            .build();
    }
}
