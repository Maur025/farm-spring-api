package com.kernotec.farm.inventory.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.inventory.jpa.entity.Farm;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmRepository extends BaseRepository<Farm, UUID> {

    Optional<Farm> findByCode(String code);
}
