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

    @Getter
    @Setter
    public static class Server {

        private String url;
        private String description;
    }
}
