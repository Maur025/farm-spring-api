package com.kernotec.farm.inventory.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.inventory.jpa.entity.DeviceConnection;
import com.kernotec.farm.inventory.jpa.repository.DeviceConnectionRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DeviceConnectionService extends BaseServiceImpl<DeviceConnection, UUID> {

    private final DeviceConnectionRepository repository;

    @Override
    protected String resourceName() {
        return "Device Connection";
    }

    @Override
    protected BaseRepository<DeviceConnection, UUID> repository() {
        return repository;
    }
}
