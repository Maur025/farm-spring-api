package com.kernotec.farm.activity.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.activity.jpa.entity.Reaction;
import com.kernotec.farm.activity.jpa.repository.ReactionRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReactionService extends BaseServiceImpl<Reaction, UUID> {

    private final ReactionRepository repository;

    @Override
    protected String resourceName() {
        return "Reaction";
    }

    @Override
    protected BaseRepository<Reaction, UUID> repository() {
        return repository;
    }
}
