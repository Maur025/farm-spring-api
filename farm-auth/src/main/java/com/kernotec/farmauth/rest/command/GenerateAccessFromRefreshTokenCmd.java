package com.kernotec.farmauth.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farmauth.command.TokenBuildClaimsSetCmd;
import com.kernotec.farmauth.command.TokenGenerateNewCmd;
import com.kernotec.farmauth.command.TokenGetClaimSetCmd;
import com.kernotec.farmauth.config.AuthConfigProperties;
import com.kernotec.farmauth.jpa.entity.Token;
import com.kernotec.farmauth.jpa.entity.User;
import com.kernotec.farmauth.jpa.enums.TokenTypeEnum;
import com.kernotec.farmauth.jpa.service.TokenService;
import com.kernotec.farmauth.jpa.service.UserService;
import com.kernotec.farmauth.rest.dto.response.OpenIdConnectTokenResponse;
import com.kernotec.farmauth.util.TimeMeasureUtil;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
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
public class GenerateAccessFromRefreshTokenCmd extends
    AbstractTransactionalRequiredCommand<GenerateAccessFromRefreshTokenCmd.Request, OpenIdConnectTokenResponse>
{

    private final AuthConfigProperties authConfigProperties;
    private final TokenGetClaimSetCmd tokenGetClaimSetCmd;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final TokenBuildClaimsSetCmd tokenBuildClaimsSetCmd;
    private final TokenGenerateNewCmd tokenGenerateNewCmd;

    @Override
    protected OpenIdConnectTokenResponse run(Request request) {
        JWTClaimsSet jwtClaimsSet = tokenGetClaimSetCmd.withRequest(
                TokenGetClaimSetCmd.Request.builder()
                    .token(request.getRefreshToken())
                    .build())
            .execute();

        Date expiration = jwtClaimsSet.getExpirationTime();

        if (expiration.before(new Date())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, " Refresh token expired");
        }

        Token token = tokenService.findByUserIdAndTokenIdAndRevokedThrow(
            UUID.fromString(jwtClaimsSet.getSubject()), UUID.fromString(jwtClaimsSet.getJWTID()),
            false
        );

        if (!passwordEncoder.matches(request.getRefreshToken(), token.getTokenHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token invalid");
        }

        User user = userService.findByIdThrow(UUID.fromString(jwtClaimsSet.getSubject()));
        long accessExp = TimeMeasureUtil.getMillisecondsByTypeTime(
            authConfigProperties.getAuthTokenExp(), authConfigProperties.getAuthTokenTypeExp());

        JWTClaimsSet claimsSetOfAccessToken = tokenBuildClaimsSetCmd.withRequest(
                TokenBuildClaimsSetCmd.Request.builder()
                    .user(user)
                    .tokenExp(accessExp)
                    .build())
            .execute();

        String accessToken = tokenGenerateNewCmd.withRequest(TokenGenerateNewCmd.Request.builder()
                .claimsSet(claimsSetOfAccessToken)
                .build())
            .execute();

        return OpenIdConnectTokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(request.getRefreshToken())
            .tokenType(TokenTypeEnum.bearer)
            .expiresIn(accessExp / 1000)
            .scope("openid profile email")
            .build();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String refreshToken;
    }
}
