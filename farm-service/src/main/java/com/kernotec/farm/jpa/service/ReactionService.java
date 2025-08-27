package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.Reaction;
import com.kernotec.farm.jpa.repository.ReactionRepository;
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
