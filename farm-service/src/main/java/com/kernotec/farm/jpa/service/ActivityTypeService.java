package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.ActivityType;
import com.kernotec.farm.jpa.repository.ActivityTypeRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ActivityTypeService extends BaseServiceImpl<ActivityType, UUID> {

    private final ActivityTypeRepository repository;

    @Override
    protected String resourceName() {
        return "Activity Type";
    }

    @Override
    protected BaseRepository<ActivityType, UUID> repository() {
        return repository;
    }
}
