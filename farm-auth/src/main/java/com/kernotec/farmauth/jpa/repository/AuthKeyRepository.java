package com.kernotec.farmauth.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farmauth.jpa.entity.AuthKey;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthKeyRepository extends BaseRepository<AuthKey, UUID> {

    Optional<AuthKey> findByRealmAndActive(String realm, boolean active);
}
