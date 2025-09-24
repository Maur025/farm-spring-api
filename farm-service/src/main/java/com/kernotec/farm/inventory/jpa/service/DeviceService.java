package com.kernotec.farm.inventory.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.jpa.repository.DeviceRepository;
import com.kernotec.farm.inventory.jpa.specification.device.DeviceSpecification;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DeviceService extends BaseServiceImpl<Device, UUID> {

    private final DeviceRepository repository;

    @Override
    protected String resourceName() {
        return "Device";
    }

    @Override
    protected BaseRepository<Device, UUID> repository() {
        return repository;
    }

    public Page<Device> findAllWithFilters(String keyword, UUID socialNetworkId, Pageable pageable)
    {
        return repository.findAll(
            DeviceSpecification.builder()
                .withSocialNetworkId(socialNetworkId)
                .withKeyword(keyword), pageable
        );
    }

    public List<Device> findAllWithMinData(UUID farmId) {

        return repository.findAll(DeviceSpecification.builder()
            .withFarmId(farmId)
            .withOrderBy("deviceNumber", false));
    }

    public Optional<Device> findByDeviceNumber(String deviceNumber) {
        return repository.findByDeviceNumber(deviceNumber);
    }
}
