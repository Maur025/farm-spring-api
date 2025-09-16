package com.kernotec.farmauth.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kernotec")
@Getter
@Setter
public class FarmAppProperties {

    private List<Server> servers;
    private String allowedOrigins;

    public List<String> getAllowedOriginsList() {
        if (allowedOrigins == null || allowedOrigins.isBlank()) {
            return List.of();
        }

        return List.of(allowedOrigins.split(","));
    }

    @Getter
    @Setter
    public static class Server {

        private String url;
        private String description;
    }
}
