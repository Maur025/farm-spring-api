package com.kernotec.farmauth.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farmauth.command.TokenGetClaimSetCmd;
import com.kernotec.farmauth.rest.dto.response.OpenIdConnectUserInfoResponse;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoGetDataCmd extends
    AbstractTransactionalRequiredCommand<UserInfoGetDataCmd.Request, OpenIdConnectUserInfoResponse>
{

    private final TokenGetClaimSetCmd tokenGetClaimSetCmd;

    @Override
    protected OpenIdConnectUserInfoResponse run(Request request) {
        JWTClaimsSet jwtClaimsSet = tokenGetClaimSetCmd.withRequest(
                TokenGetClaimSetCmd.Request.builder()
                    .token(request.getAccessToken())
                    .build())
            .execute();

        Date expiration = jwtClaimsSet.getExpirationTime();

        if (expiration.before(new Date())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, " Refresh token expired");
        }

        return OpenIdConnectUserInfoResponse.builder()
            .sub(jwtClaimsSet.getSubject())
            .username(jwtClaimsSet.getClaim("preferred_username")
                .toString())
            .name(jwtClaimsSet.getClaim("name")
                .toString())
            .roles(List.of("ADMIN"))
            .build();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String accessToken;
    }
}
