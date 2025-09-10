package com.kernotec.farmauth.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farmauth.jpa.entity.Token;
import com.kernotec.farmauth.jpa.repository.TokenRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
}
