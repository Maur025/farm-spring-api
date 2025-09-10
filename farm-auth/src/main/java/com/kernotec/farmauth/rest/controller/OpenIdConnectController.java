package com.kernotec.farmauth.rest.controller;

import com.kernotec.farmauth.jpa.enums.GrantTypeEnum;
import com.kernotec.farmauth.jpa.enums.TokenTypeEnum;
import com.kernotec.farmauth.rest.ApiSpec.OpenIdConnectSpec;
import com.kernotec.farmauth.rest.dto.request.OpenIdConnectTokenRequest;
import com.kernotec.farmauth.rest.dto.response.OpenIdConnectTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = OpenIdConnectSpec.TAG_NAME, description = OpenIdConnectSpec.TAG_DESCRIPTION)
@RequestMapping(path = OpenIdConnectSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class OpenIdConnectController {

    @Operation(summary = "OpenID Connect Endpoint to get token")
    @PostMapping("token")
    @ResponseStatus(HttpStatus.OK)
    public OpenIdConnectTokenResponse getTokenByGrantType(
        @RequestParam("grant_type") GrantTypeEnum grantType,
        @RequestBody OpenIdConnectTokenRequest request)
    {
        log.info("GRANT TYPE: {}", grantType);
        log.info("REQUEST username: {}", request.getUsername());
        log.info("REQUEST password: {}", request.getPassword());

        return OpenIdConnectTokenResponse.builder()
            .accessToken("token test")
            .refreshToken("refresh token test")
            .tokenType(TokenTypeEnum.bearer)
            .expiresIn(3600L)
            .refreshExpiresIn(1800L)
            .scope("openid email profile")
            .build();
    }
}
