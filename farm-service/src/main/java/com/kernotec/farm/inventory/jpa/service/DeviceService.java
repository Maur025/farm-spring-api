package com.kernotec.farm.inventory.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.jpa.repository.DeviceRepository;
import com.kernotec.farm.inventory.jpa.specification.device.DeviceSpecification;
import com.kernotec.farm.inventory.rest.dto.response.device.DeviceMinResponse;
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

    public Page<DeviceMinResponse> findAllWithMinData(UUID farmId, String keyword,
        Pageable pageable)
    {
        String keywordStr = (keyword == null || keyword.isBlank()) ? null : keyword.trim();

        return repository.findAllMinData(farmId, keywordStr, pageable);
    }

    public Optional<Device> findByDeviceNumber(String deviceNumber) {
        return repository.findByDeviceNumber(deviceNumber);
    }
}
