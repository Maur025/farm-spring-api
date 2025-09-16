package com.kernotec.farm.activity.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.activity.jpa.entity.Follow;
import com.kernotec.farm.activity.jpa.repository.FollowRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FollowService extends BaseServiceImpl<Follow, UUID> {

    private final FollowRepository repository;

    @Override
    protected String resourceName() {
        return "Follow";
    }

    @Override
    protected BaseRepository<Follow, UUID> repository() {
        return repository;
    }
}
