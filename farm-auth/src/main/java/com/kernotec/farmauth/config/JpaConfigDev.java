package com.kernotec.farmauth.config;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class JpaConfigDev {

    @Bean("auditorAware")
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("farm-test-user");
    }
}
