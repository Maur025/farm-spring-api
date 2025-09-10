package com.kernotec.farmauth.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farmauth.command.TokenCreateCmd;
import com.kernotec.farmauth.config.AuthConfigProperties;
import com.kernotec.farmauth.config.FarmAppProperties;
import com.kernotec.farmauth.jpa.entity.User;
import com.kernotec.farmauth.jpa.enums.TokenTypeEnum;
import com.kernotec.farmauth.jpa.service.UserService;
import com.kernotec.farmauth.rest.dto.request.OpenIdConnectTokenRequest;
import com.kernotec.farmauth.rest.dto.response.OpenIdConnectTokenResponse;
import com.kernotec.farmauth.util.TimeMeasureUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

        JWTClaimsSet claimsSetOfAccessToken = getClaimsSet(user, accessExp, null);
        JWTClaimsSet claimsSetOfRefreshToken = getClaimsSet(
            user, refreshExp, refreshTokenId.toString());

        String accessToken = generateToken(claimsSetOfAccessToken);
        String refreshToken = generateToken(claimsSetOfRefreshToken);

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

    private JWTClaimsSet getClaimsSet(User user, long tokenExp, String refreshTokenId)
    {
        String hostUrl = farmAppProperties.getServers()
            .get(0)
            .getUrl();

        return new JWTClaimsSet.Builder().subject(user.getId()
                .toString())
            .claim("preferred_username", user.getUsername())
            .claim("name", user.getName() + " " + user.getLastName())
            .claim("realm_access", Map.of("roles", List.of("ADMIN")))
            .jwtID(refreshTokenId != null ? refreshTokenId : UUID.randomUUID()
                .toString())
            .claim(
                "auth_time", Instant.now()
                    .getEpochSecond()
            )
            .claim("azp", "farm-frontend-app")
            .claim("scope", "openid profile email")
            .issueTime(new Date())
            .expirationTime(new Date(System.currentTimeMillis() + tokenExp))
            .issuer(hostUrl)
            .audience(authConfigProperties.getAudience())
            .notBeforeTime(new Date())
            .build();
    }

    private String generateToken(JWTClaimsSet claimsSet) {
        try {
            JWSSigner signer = new MACSigner(authConfigProperties.getSecretKey()
                .getBytes());

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Error generating token: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final OpenIdConnectTokenRequest authRequest;
    }
}
