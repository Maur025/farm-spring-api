package com.kernotec.farm.parametric.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.parametric.exception.SocialNetworkException;
import com.kernotec.farm.parametric.jpa.entity.SocialNetwork;
import com.kernotec.farm.parametric.jpa.repository.SocialNetworkRepository;
import com.kernotec.farm.parametric.rest.dto.response.social.network.SocialNetworkMinResponse;
import jakarta.persistence.criteria.JoinType;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SocialNetworkService extends BaseServiceImpl<SocialNetwork, UUID> {

    private final SocialNetworkRepository repository;

    @Override
    protected String resourceName() {
        return "Social Network";
    }

    @Override
    protected BaseRepository<SocialNetwork, UUID> repository() {
        return repository;
    }

    public Optional<SocialNetwork> findByCode(String code) {
        return repository.findByCode(code);
    }

    public SocialNetwork findByCodeThrow(String code) {
        return findByCode(code).orElseThrow(
            () -> new SocialNetworkException(
                "code.not.found", "'" + code + "'", HttpStatus.NOT_FOUND.value()));
    }

    public Page<SocialNetwork> findAllWithFilters(Boolean isHasAccounts, Boolean isHasActivityTypes,
        Pageable pageable)
    {
        return repository.findAll(
            (Specification<SocialNetwork>) (root, query, cb) -> {

                if (isHasAccounts != null && isHasAccounts.equals(Boolean.TRUE)) {
                    root.join("accounts", JoinType.INNER);
                }

                if (isHasActivityTypes != null && isHasActivityTypes.equals(Boolean.TRUE)) {
                    root.join("activityTypes", JoinType.INNER);
                }

                query.distinct(true);
                return cb.conjunction();
            }, pageable
        );
    }

    public Page<SocialNetworkMinResponse> findAllMinData(Boolean isHasAccounts,
        Boolean isHasActivityTypes, String keyword, Pageable pageable)
    {
        String keywordStr = (keyword == null || keyword.isBlank()) ? null : keyword.trim();

        return repository.findAllMinData(isHasAccounts, isHasActivityTypes, keywordStr, pageable);
    }
}
