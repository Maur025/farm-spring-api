package com.kernotec.farm.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.jpa.entity.Reaction;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends BaseRepository<Reaction, UUID> {

}
