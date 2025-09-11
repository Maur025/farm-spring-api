package com.kernotec.farmauth.command;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.farmauth.config.AuthConfigProperties;
import com.kernotec.farmauth.config.FarmAppProperties;
import com.kernotec.farmauth.jpa.entity.User;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenBuildClaimsSetCmd extends
    AbstractCommand<TokenBuildClaimsSetCmd.Request, JWTClaimsSet>
{

    private final FarmAppProperties farmAppProperties;
    private final AuthConfigProperties authConfigProperties;

    @Override
    protected JWTClaimsSet run(Request request) {
        User user = request.getUser();

        String hostUrl = farmAppProperties.getServers()
            .get(0)
            .getUrl();

        return new JWTClaimsSet.Builder().subject(user.getId()
                .toString())
            .claim("preferred_username", user.getUsername())
            .claim("name", user.getName() + " " + user.getLastName())
            .claim("realm_access", Map.of("roles", List.of("ADMIN")))
            .jwtID(request.getRefreshTokenId() != null ? request.getRefreshTokenId()
                : UUID.randomUUID()
                    .toString())
            .claim(
                "auth_time", Instant.now()
                    .getEpochSecond()
            )
            .claim("azp", "farm-frontend-app")
            .claim("scope", "openid profile email")
            .issueTime(new Date())
            .expirationTime(new Date(System.currentTimeMillis() + request.getTokenExp()))
            .issuer(hostUrl)
            .audience(authConfigProperties.getAudience())
            .notBeforeTime(new Date())
            .build();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final User user;
        @NotNull
        private final Long tokenExp;
        private final String refreshTokenId;
    }
}
