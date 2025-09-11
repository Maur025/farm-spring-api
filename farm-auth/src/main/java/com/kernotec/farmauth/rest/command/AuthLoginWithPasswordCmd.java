package com.kernotec.farmauth.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farmauth.command.TokenBuildClaimsSetCmd;
import com.kernotec.farmauth.command.TokenCreateCmd;
import com.kernotec.farmauth.command.TokenGenerateNewCmd;
import com.kernotec.farmauth.config.AuthConfigProperties;
import com.kernotec.farmauth.config.FarmAppProperties;
import com.kernotec.farmauth.jpa.entity.User;
import com.kernotec.farmauth.jpa.enums.TokenTypeEnum;
import com.kernotec.farmauth.jpa.service.UserService;
import com.kernotec.farmauth.rest.dto.request.OpenIdConnectTokenRequest;
import com.kernotec.farmauth.rest.dto.response.OpenIdConnectTokenResponse;
import com.kernotec.farmauth.util.TimeMeasureUtil;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthLoginWithPasswordCmd extends
    AbstractTransactionalRequiredCommand<AuthLoginWithPasswordCmd.Request, OpenIdConnectTokenResponse>
{

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthConfigProperties authConfigProperties;
    private final FarmAppProperties farmAppProperties;
    private final TokenCreateCmd tokenCreateCmd;
    private final TokenGenerateNewCmd tokenGenerateNewCmd;
    private final TokenBuildClaimsSetCmd tokenBuildClaimsSetCmd;

    @Override
    protected OpenIdConnectTokenResponse run(Request request) {
        OpenIdConnectTokenRequest authRequest = request.getAuthRequest();

        User user = userService.findByUsernameThrow(authRequest.getUsername());

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Username or Password invalid");
        }

        long accessExp = TimeMeasureUtil.getMillisecondsByTypeTime(
            authConfigProperties.getAuthTokenExp(), authConfigProperties.getAuthTokenTypeExp());

        long refreshExp = TimeMeasureUtil.getMillisecondsByTypeTime(
            authConfigProperties.getRefreshTokenExp(),
            authConfigProperties.getRefreshTokenTypeExp()
        );

        UUID refreshTokenId = UUID.randomUUID();

        JWTClaimsSet claimsSetOfAccessToken = tokenBuildClaimsSetCmd.withRequest(
                TokenBuildClaimsSetCmd.Request.builder()
                    .user(user)
                    .tokenExp(accessExp)
                    .build())
            .execute();

        JWTClaimsSet claimsSetOfRefreshToken = tokenBuildClaimsSetCmd.withRequest(
                TokenBuildClaimsSetCmd.Request.builder()
                    .user(user)
                    .tokenExp(refreshExp)
                    .refreshTokenId(refreshTokenId.toString())
                    .build())
            .execute();

        String accessToken = tokenGenerateNewCmd.withRequest(TokenGenerateNewCmd.Request.builder()
                .claimsSet(claimsSetOfAccessToken)
                .build())
            .execute();

        String refreshToken = tokenGenerateNewCmd.withRequest(TokenGenerateNewCmd.Request.builder()
                .claimsSet(claimsSetOfRefreshToken)
                .build())
            .execute();

        tokenCreateCmd.withRequest(TokenCreateCmd.Request.builder()
                .clientId("farm-frontend-app")
                .tokenHash(refreshToken)
                .tokenId(refreshTokenId)
                .issuedAt(ZonedDateTime.now())
                .expiresAt(ZonedDateTime.now()
                    .plus(Duration.ofMillis(refreshExp)))
                .expiresIn(refreshExp / 1000)
                .revoked(false)
                .userId(user.getId())
                .build())
            .execute();

        return OpenIdConnectTokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType(TokenTypeEnum.bearer)
            .expiresIn(accessExp / 1000)
            .refreshExpiresIn(refreshExp / 1000)
            .scope("openid profile email")
            .build();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final OpenIdConnectTokenRequest authRequest;
    }
}
