package com.kernotec.farm.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.jpa.entity.Publishing;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishingRepository extends BaseRepository<Publishing, UUID> {

}
