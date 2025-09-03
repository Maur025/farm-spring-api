package com.kernotec.farm.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.jpa.entity.Connection;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends BaseRepository<Connection, UUID> {

}
