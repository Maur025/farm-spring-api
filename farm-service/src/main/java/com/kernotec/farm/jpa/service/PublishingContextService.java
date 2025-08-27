package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.PublishingContext;
import com.kernotec.farm.jpa.repository.PublishingContextRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PublishingContextService extends BaseServiceImpl<PublishingContext, UUID> {

    private final PublishingContextRepository repository;

    @Override
    protected String resourceName() {
        return "Publishing Context";
    }

    @Override
    protected BaseRepository<PublishingContext, UUID> repository() {
        return repository;
    }
}
