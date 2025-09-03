package com.kernotec.farm.parametric.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.parametric.jpa.entity.PublishingType;
import com.kernotec.farm.parametric.jpa.repository.PublishingTypeRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PublishingTypeService extends BaseServiceImpl<PublishingType, UUID> {

    private final PublishingTypeRepository repository;

    @Override
    protected String resourceName() {
        return "Publishing Type";
    }

    @Override
    protected BaseRepository<PublishingType, UUID> repository() {
        return repository;
    }
}
