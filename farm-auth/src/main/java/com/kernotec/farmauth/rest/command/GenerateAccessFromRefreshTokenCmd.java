package com.kernotec.farmauth.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farmauth.config.AuthConfigProperties;
import com.kernotec.farmauth.rest.dto.response.OpenIdConnectTokenResponse;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.constraints.NotNull;
import java.text.ParseException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GenerateAccessFromRefreshTokenCmd extends
    AbstractTransactionalRequiredCommand<GenerateAccessFromRefreshTokenCmd.Request, OpenIdConnectTokenResponse>
{

    private final AuthConfigProperties authConfigProperties;

    @Override
    protected OpenIdConnectTokenResponse run(Request request) {
        log.info("refresh token: {}", request.getRefreshToken());

        return OpenIdConnectTokenResponse.builder()
            .accessToken("nuevo access token")
            .build();
    }

    public void readToken(String token) {

        try {
            JWSSigner signer = new MACSigner(authConfigProperties.getSecretKey()
                .getBytes());

            SignedJWT signedJWT = SignedJWT.parse(token);

        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String refreshToken;
    }
}
