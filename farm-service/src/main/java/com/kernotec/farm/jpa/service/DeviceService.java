package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.Device;
import com.kernotec.farm.jpa.repository.DeviceRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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

    public List<Device> findAllByKeyword(String keyword) {
        String pattern = keyword == null ? null : "%" + keyword.toLowerCase() + "%";

        return repository.findAll((Specification<Device>) (root, query, cb) -> {
            if (pattern == null) {
                return cb.conjunction();
            } else {
                return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("deviceNumber")), pattern)
                );
            }
        });
    }
}
