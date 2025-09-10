package com.kernotec.farm.parametric.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.parametric.exception.ActivityTypeException;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.parametric.jpa.entity.SocialNetwork;
import com.kernotec.farm.parametric.jpa.enums.ActivityTypeCodeEnum;
import com.kernotec.farm.parametric.jpa.repository.ActivityTypeRepository;
import jakarta.persistence.criteria.Join;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
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

    public Optional<ActivityType> findByCode(ActivityTypeCodeEnum code) {
        return repository.findByCode(code.toString());
    }

    public ActivityType findByCodeThrow(ActivityTypeCodeEnum code) {
        return findByCode(code).orElseThrow(
            () -> new ActivityTypeException(
                "code.not.found", "'" + code + "'", HttpStatus.NOT_FOUND.value()));
    }
}
