package com.kernotec.farmauth.rest.controller;

import com.kernotec.farmauth.jpa.enums.GrantTypeEnum;
import com.kernotec.farmauth.rest.ApiSpec.OpenIdConnectSpec;
import com.kernotec.farmauth.rest.command.ConnectionTokenCmd;
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

    private final ConnectionTokenCmd connectionTokenCmd;

    @Operation(summary = "OpenID Connect Endpoint to get token")
    @PostMapping("token")
    @ResponseStatus(HttpStatus.OK)
    public OpenIdConnectTokenResponse getTokenByGrantType(
        @RequestParam("grant_type") GrantTypeEnum grantType,
        @RequestBody(required = false) OpenIdConnectTokenRequest request)
    {
        return connectionTokenCmd.withRequest(ConnectionTokenCmd.Request.builder()
                .grantType(grantType)
                .tokenRequest(request)
                .build())
            .execute();
    }
}
