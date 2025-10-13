package com.kernotec.farm.swagger;

import com.kernotec.farm.config.KernotecApiDefinition;
import com.kernotec.farm.config.KernotecApiDefinition.OpenApiInfo;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KernotecApiDefinition.class)
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer",
                bearerFormat = "JWT")
public class OpenApi3Config {

    @Bean
    @ConditionalOnMissingBean
    public OpenAPI openApiDefinition(KernotecApiDefinition kernotecApiDefinition) {
        OpenApiInfo openApiInfo = kernotecApiDefinition.getInfo();

        OpenAPI openAPI = new OpenAPI().info(new Info().title(openApiInfo.getTitle())
                .description(openApiInfo.getDescription())
                .version(openApiInfo.getVersion()))
            .security(List.of(new SecurityRequirement().addList("bearerAuth")));

        kernotecApiDefinition.getServers()
            .forEach(openApiServer -> {
                if (Strings.isNotBlank(openApiServer.getUrl())) {
                    openAPI.addServersItem(new Server().url(openApiServer.getUrl())
                        .description(openApiServer.getDescription()));
                }
            });

        return openAPI;
    }
}
