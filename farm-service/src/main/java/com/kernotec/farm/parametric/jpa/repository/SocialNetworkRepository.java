package com.kernotec.farm.parametric.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.parametric.jpa.entity.SocialNetwork;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialNetworkRepository extends BaseRepository<SocialNetwork, UUID> {

    Optional<SocialNetwork> findByCode(String code);
}
