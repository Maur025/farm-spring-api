package com.kernotec.farm.parametric.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.parametric.jpa.entity.PublishingType;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishingTypeRepository extends BaseRepository<PublishingType, UUID> {

}
