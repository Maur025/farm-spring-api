package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.AssignedChip;
import com.kernotec.farm.jpa.repository.AssignedChipRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AssignedChipService extends BaseServiceImpl<AssignedChip, UUID> {

    private final AssignedChipRepository repository;

    @Override
    protected String resourceName() {
        return "Assigned Chip";
    }

    @Override
    protected BaseRepository<AssignedChip, UUID> repository() {
        return repository;
    }
}
