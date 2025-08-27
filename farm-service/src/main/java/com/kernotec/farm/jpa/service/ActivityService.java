package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.Activity;
import com.kernotec.farm.jpa.repository.ActivityRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ActivityService extends BaseServiceImpl<Activity, UUID> {

    private final ActivityRepository repository;

    @Override
    protected String resourceName() {
        return "Activity";
    }

    @Override
    protected BaseRepository<Activity, UUID> repository() {
        return repository;
    }
}
