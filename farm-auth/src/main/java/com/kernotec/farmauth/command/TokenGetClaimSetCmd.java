package com.kernotec.farmauth.command;

import com.kernotec.core.command.AbstractCommand;
import com.nimbusds.jwt.JWTClaimsSet;
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
public class TokenGetClaimSetCmd extends
    AbstractCommand<TokenGetClaimSetCmd.Request, JWTClaimsSet>
{

    @Override
    protected JWTClaimsSet run(Request request) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(request.getToken());
            return signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            log.error("Error parsing token", e);
            throw new RuntimeException(e);
        }
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String token;
    }
}
