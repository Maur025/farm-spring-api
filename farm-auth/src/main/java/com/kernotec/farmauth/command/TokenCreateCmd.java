package com.kernotec.farmauth.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farmauth.jpa.entity.Token;
import com.kernotec.farmauth.jpa.service.TokenService;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenCreateCmd extends
    AbstractTransactionalRequiredCommand<TokenCreateCmd.Request, UUID>
{

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected UUID run(Request request) {
        Token token = new Token();

        token.setClientId(request.getClientId());
        token.setTokenHash(passwordEncoder.encode(request.getTokenHash()));
        token.setTokenId(request.getTokenId());
        token.setIssuedAt(request.getIssuedAt());
        token.setExpiresAt(request.getExpiresAt());
        token.setExpiresIn(request.getExpiresIn());
        token.setRevoked(request.getRevoked());
        token.setUserId(request.getUserId());

        token = tokenService.save(token);
        return token.getId();
    }

    @Builder
    @Getter
    public static class Request {

        private String clientId;
        @NotNull
        private String tokenHash;
        @NotNull
        private UUID tokenId;
        @NotNull
        private ZonedDateTime issuedAt;
        @NotNull
        private ZonedDateTime expiresAt;
        @NotNull
        private Long expiresIn;
        @NotNull
        private Boolean revoked;
        @NotNull
        private UUID userId;
    }
}
