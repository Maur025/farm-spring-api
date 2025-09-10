package com.kernotec.farmauth.rest.controller;

import com.kernotec.farmauth.config.AuthConfigProperties;
import com.kernotec.farmauth.jpa.enums.GrantTypeEnum;
import com.kernotec.farmauth.jpa.enums.RefreshTokenSecureEnum;
import com.kernotec.farmauth.rest.ApiSpec.OpenIdConnectSpec;
import com.kernotec.farmauth.rest.command.ConnectionTokenCmd;
import com.kernotec.farmauth.rest.dto.request.OpenIdConnectTokenRequest;
import com.kernotec.farmauth.rest.dto.response.OpenIdConnectTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
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
    private final AuthConfigProperties authConfigProperties;

    @Operation(summary = "OpenID Connect Endpoint to get token")
    @PostMapping("token")
    @ResponseStatus(HttpStatus.OK)
    public OpenIdConnectTokenResponse getTokenByGrantType(
        @RequestParam("grant_type") GrantTypeEnum grantType,
        @RequestBody(required = false) OpenIdConnectTokenRequest request,
        HttpServletResponse response,
        @CookieValue(value = "refresh_token", required = false) String refreshToken)
    {
        OpenIdConnectTokenResponse openIdConnectTokenResponse = connectionTokenCmd.withRequest(
                ConnectionTokenCmd.Request.builder()
                    .grantType(grantType)
                    .tokenRequest(request)
                    .refreshToken(refreshToken)
                    .build())
            .execute();

        if (grantType.equals(GrantTypeEnum.password) && openIdConnectTokenResponse != null
            && openIdConnectTokenResponse.getRefreshToken() != null)
        {
            Cookie refreshTokenCookie = new Cookie(
                "refresh_token", openIdConnectTokenResponse.getRefreshToken());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(
                RefreshTokenSecureEnum.prod.equals(authConfigProperties.getRefreshTokenSecure()));
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(
                Math.toIntExact(openIdConnectTokenResponse.getRefreshExpiresIn()));

            response.addCookie(refreshTokenCookie);
        }

        return openIdConnectTokenResponse;
    }
}
