package com.kernotec.farm.inventory.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.inventory.jpa.entity.DeviceImei;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceImeiRepository extends BaseRepository<DeviceImei, UUID> {

}
