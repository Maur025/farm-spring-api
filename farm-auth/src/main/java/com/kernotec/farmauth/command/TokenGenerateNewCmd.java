package com.kernotec.farmauth.command;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.farmauth.config.AuthConfigProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenGenerateNewCmd extends AbstractCommand<TokenGenerateNewCmd.Request, String> {

    private final AuthConfigProperties authConfigProperties;

    @Override
    protected String run(Request request) {
        try {
            JWSSigner signer = new MACSigner(authConfigProperties.getSecretKey()
                .getBytes());

            SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256), request.getClaimsSet());

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
        private final JWTClaimsSet claimsSet;
    }
}
