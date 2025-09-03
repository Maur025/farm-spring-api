package com.kernotec.farm.inventory.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.inventory.jpa.entity.Chip;
import com.kernotec.farm.inventory.jpa.repository.ChipRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ChipService extends BaseServiceImpl<Chip, UUID> {

    private final ChipRepository repository;

    @Override
    protected String resourceName() {
        return "Chip";
    }

    @Override
    protected BaseRepository<Chip, UUID> repository() {
        return repository;
    }
}
