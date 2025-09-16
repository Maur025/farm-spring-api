package com.kernotec.farm.inventory.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.inventory.jpa.entity.Device;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends BaseRepository<Device, UUID> {

    Optional<Device> findByDeviceNumber(String deviceNumber);
}
