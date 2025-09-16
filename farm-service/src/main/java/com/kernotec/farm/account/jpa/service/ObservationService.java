package com.kernotec.farm.account.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.account.jpa.entity.Observation;
import com.kernotec.farm.account.jpa.repository.ObservationRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ObservationService extends BaseServiceImpl<Observation, UUID> {

    private final ObservationRepository repository;

    @Override
    protected String resourceName() {
        return "Observation";
    }

    @Override
    protected BaseRepository<Observation, UUID> repository() {
        return repository;
    }
}
