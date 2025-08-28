package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.SocialNetworkAction;
import com.kernotec.farm.jpa.repository.SocialNetworkActionRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SocialNetworkActionService extends BaseServiceImpl<SocialNetworkAction, UUID> {

    private final SocialNetworkActionRepository repository;

    @Override
    protected String resourceName() {
        return "Social Network Action";
    }

    @Override
    protected BaseRepository<SocialNetworkAction, UUID> repository() {
        return repository;
    }
}
