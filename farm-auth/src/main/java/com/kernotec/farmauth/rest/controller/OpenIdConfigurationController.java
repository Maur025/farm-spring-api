package com.kernotec.farmauth.rest.controller;

import com.kernotec.farmauth.config.FarmAppProperties;
import com.kernotec.farmauth.rest.ApiSpec.OpenIdConfigurationSpec;
import com.kernotec.farmauth.rest.dto.response.OpenIdConfigurationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = OpenIdConfigurationSpec.TAG_NAME, description = OpenIdConfigurationSpec.TAG_DESCRIPTION)
@RequestMapping(path = OpenIdConfigurationSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class OpenIdConfigurationController {

    private final FarmAppProperties farmAppProperties;

    @Operation(summary = "Retrieve OpenID Configuration")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public OpenIdConfigurationResponse getOpenIdConfiguration() {
        String serverHost = farmAppProperties.getServers()
            .get(0)
            .getUrl();

        String realm = "/realms/farm-auth";

        return OpenIdConfigurationResponse.builder()
            .issuer(serverHost + realm)
            .authorization_endpoint(serverHost + realm + "/protocol/openid-connect/auth")
            .token_endpoint(serverHost + realm + "/protocol/openid-connect/token")
            .userinfo_endpoint(serverHost + realm + "/protocol/openid-connect/userinfo")
            .jwks_uri(serverHost + realm + "/protocol/openid-connect/certs")
            .response_types_supported(List.of("code", "token", "id_token"))
            .subject_types_supported(List.of("public"))
            .id_token_signing_alg_values_supported(List.of("RS256"))
            .build();
    }
}
