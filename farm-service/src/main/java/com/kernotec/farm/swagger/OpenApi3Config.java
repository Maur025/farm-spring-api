package com.kernotec.farm.swagger;

import com.kernotec.farm.swagger.KernotecApiDefinition.OpenApiInfo;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
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
@SecurityScheme(name = "oauth2Password", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(
    password = @OAuthFlow(
        tokenUrl = "${oauth2-config.host}/realms/${oauth2-config.realm}/protocol/openid-connect/token",
        refreshUrl = "${oauth2-config.host}/realms/${oauth2-config.realm}/protocol/openid-connect/token",
        scopes = {@OAuthScope(name = "openid", description = "openid"),
            @OAuthScope(name = "profile", description = "profile")})))
public class OpenApi3Config {

    @Bean
    @ConditionalOnMissingBean
    public OpenAPI openApiDefinition(KernotecApiDefinition kernotecApiDefinition) {
        OpenApiInfo openApiInfo = kernotecApiDefinition.getInfo();

        OpenAPI openAPI = new OpenAPI().info(new Info().title(openApiInfo.getTitle())
                .description(openApiInfo.getDescription())
                .version(openApiInfo.getVersion()))
            .security(List.of(new SecurityRequirement().addList("oauth2Password")));

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
