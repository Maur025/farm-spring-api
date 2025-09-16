package com.kernotec.farm.inventory.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.inventory.jpa.entity.DeviceImei;
import com.kernotec.farm.inventory.jpa.repository.DeviceImeiRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DeviceImeiService extends BaseServiceImpl<DeviceImei, UUID> {

    private final DeviceImeiRepository repository;

    @Override
    protected String resourceName() {
        return "Device Imei";
    }

    @Override
    protected BaseRepository<DeviceImei, UUID> repository() {
        return repository;
    }
}
