package com.kernotec.farm.activity.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.activity.jpa.entity.Group;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends BaseRepository<Group, UUID> {

}
