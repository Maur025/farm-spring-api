package com.kernotec.farm.config;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtDecoderConfig {

    private final AuthConfigProperties authConfigProperties;

    public JwtDecoderConfig(AuthConfigProperties authConfigProperties) {
        this.authConfigProperties = authConfigProperties;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(
            authConfigProperties.getSecretKey()
                .getBytes(), "HmacSHA256"
        );

        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
            .build();
    }
}
