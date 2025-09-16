package com.kernotec.farm.parametric.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.parametric.jpa.entity.GroupState;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupStateRepository extends BaseRepository<GroupState, UUID> {

    Optional<GroupState> findByCode(String code);
}
