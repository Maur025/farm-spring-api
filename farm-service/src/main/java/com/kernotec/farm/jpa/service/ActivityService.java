package com.kernotec.farm.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.jpa.entity.Activity;
import com.kernotec.farm.jpa.repository.ActivityRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ActivityService extends BaseServiceImpl<Activity, UUID> {

    private final ActivityRepository repository;

    @Override
    protected String resourceName() {
        return "Activity";
    }

    @Override
    protected BaseRepository<Activity, UUID> repository() {
        return repository;
    }

    public Page<Activity> findAllWithFilters(UUID accountId, Pageable pageable) {
        return repository.findAll(
            (Specification<Activity>) (root, query, cb) -> {
                if (accountId == null) {
                    return cb.conjunction();
                }

                return cb.or(cb.equal(root.get("accountId"), accountId));
            }, pageable
        );
    }
}
