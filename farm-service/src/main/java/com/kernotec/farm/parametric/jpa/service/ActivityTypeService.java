package com.kernotec.farm.parametric.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.parametric.jpa.entity.SocialNetwork;
import com.kernotec.farm.parametric.jpa.repository.ActivityTypeRepository;
import jakarta.persistence.criteria.Join;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ActivityTypeService extends BaseServiceImpl<ActivityType, UUID> {

    private final ActivityTypeRepository repository;

    @Override
    protected String resourceName() {
        return "Activity Type";
    }

    @Override
    protected BaseRepository<ActivityType, UUID> repository() {
        return repository;
    }

    public List<ActivityType> findAllWithFilters(UUID socialNetworkId) {
        if (socialNetworkId == null) {
            return repository.findAll();
        }

        return repository.findAll((Specification<ActivityType>) (root, query, cb) -> {
            Join<ActivityType, SocialNetwork> socialNetworkJoin = root.join("socialNetworks");

            return cb.or(cb.equal(socialNetworkJoin.get("id"), socialNetworkId));
        });
    }
}
