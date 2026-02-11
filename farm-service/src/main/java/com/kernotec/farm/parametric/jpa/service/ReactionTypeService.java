package com.kernotec.farm.parametric.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.parametric.jpa.entity.ReactionType;
import com.kernotec.farm.parametric.jpa.repository.ReactionTypeRepository;
import com.kernotec.farm.parametric.rest.dto.response.reaction.type.ReactionTypeMinResponse;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
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

    public Page<ReactionType> findAllWithFilters(UUID socialNetworkId, Pageable pageable) {
        if (socialNetworkId == null) {
            return repository.findAll(pageable);
        }

        return repository.findAll(
            (Specification<ReactionType>) (root, query, cb) -> cb.or(
                cb.equal(root.get("socialNetworkId"), socialNetworkId)), pageable
        );
    }

    public Page<ReactionTypeMinResponse> findAllMinData(
        @Param("socialNetworkId") UUID socialNetworkId, @Param("keyword") String keyword,
        Pageable pageable)
    {
        String keywordStr = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        return repository.findAllMinData(socialNetworkId, keywordStr, pageable);
    }
}
