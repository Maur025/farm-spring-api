package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.RequestState;
import com.kernotec.farm.jpa.repository.RequestStateRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RequestStateService extends BaseServiceImpl<RequestState, UUID> {

    private final RequestStateRepository repository;

    @Override
    protected String resourceName() {
        return "Request State";
    }

    @Override
    protected BaseRepository<RequestState, UUID> repository() {
        return repository;
    }
}
