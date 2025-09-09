package com.kernotec.farmauth.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farmauth.jpa.entity.AuthKey;
import com.kernotec.farmauth.jpa.repository.AuthKeyRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthKeyService extends BaseServiceImpl<AuthKey, UUID> {

    private final AuthKeyRepository repository;

    @Override
    protected String resourceName() {
        return "Auth Key";
    }

    @Override
    protected BaseRepository<AuthKey, UUID> repository() {
        return repository;
    }

    public Optional<AuthKey> findByRealmAndActive(String realm, boolean active) {
        return repository.findByRealmAndActive(realm, active);
    }
}
