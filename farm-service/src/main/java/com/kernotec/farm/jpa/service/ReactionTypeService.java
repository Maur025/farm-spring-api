package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.ReactionType;
import com.kernotec.farm.jpa.repository.ReactionTypeRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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

    public List<ReactionType> findAllWithFilters(UUID socialNetworkId) {
        if (socialNetworkId == null) {
            return repository.findAll();
        }

        return repository.findAll((Specification<ReactionType>) (root, query, cb) -> cb.or(
            cb.equal(root.get("socialNetworkId"), socialNetworkId)));
    }
}
