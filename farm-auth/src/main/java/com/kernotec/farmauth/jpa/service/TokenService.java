package com.kernotec.farmauth.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farmauth.jpa.entity.Token;
import com.kernotec.farmauth.jpa.repository.TokenRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
public class TokenService extends BaseServiceImpl<Token, UUID> {

    private final TokenRepository repository;

    @Override
    protected String resourceName() {
        return "token";
    }

    @Override
    protected BaseRepository<Token, UUID> repository() {
        return repository;
    }

    public Optional<Token> findByUserIdAndTokenIdAndRevoked(UUID userId, UUID tokenId,
        Boolean revoked)
    {
        return repository.findByUserIdAndTokenIdAndRevoked(userId, tokenId, revoked);
    }

    public Token findByUserIdAndTokenIdAndRevokedThrow(UUID userId, UUID tokenId, Boolean revoked) {
        return findByUserIdAndTokenIdAndRevoked(userId, tokenId, revoked).orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                String.format("Token not found for userId: %s and tokenId: %s", userId, tokenId)
            ));
    }
}
