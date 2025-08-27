package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.ReactionType;
import com.kernotec.farm.jpa.repository.ReactionTypeRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReactionTypeService extends BaseServiceImpl<ReactionType, UUID> {

    private final ReactionTypeRepository repository;

    @Override
    protected String resourceName() {
        return "Reaction Type";
    }

    @Override
    protected BaseRepository<ReactionType, UUID> repository() {
        return repository;
    }
}
