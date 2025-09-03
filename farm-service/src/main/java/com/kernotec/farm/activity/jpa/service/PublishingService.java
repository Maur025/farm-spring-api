package com.kernotec.farm.activity.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.activity.jpa.entity.Publishing;
import com.kernotec.farm.activity.jpa.repository.PublishingRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PublishingService extends BaseServiceImpl<Publishing, UUID> {

    private final PublishingRepository repository;

    @Override
    protected String resourceName() {
        return "Publishing";
    }

    @Override
    protected BaseRepository<Publishing, UUID> repository() {
        return repository;
    }
}
