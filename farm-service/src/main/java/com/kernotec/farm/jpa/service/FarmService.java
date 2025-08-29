package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.Farm;
import com.kernotec.farm.jpa.repository.FarmRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FarmService extends BaseServiceImpl<Farm, UUID> {

    private final FarmRepository repository;

    @Override
    protected String resourceName() {
        return "Farm";
    }

    @Override
    protected BaseRepository<Farm, UUID> repository() {
        return repository;
    }

    public Optional<Farm> findByCode(String code) {
        return repository.findByCode(code);
    }
}
