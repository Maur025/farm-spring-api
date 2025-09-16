package com.kernotec.farm.account.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.account.jpa.entity.AssignedChip;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignedChipRepository extends BaseRepository<AssignedChip, UUID> {

}
