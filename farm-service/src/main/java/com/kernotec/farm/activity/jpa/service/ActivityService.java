package com.kernotec.farm.activity.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.repository.ActivityRepository;
import com.kernotec.farm.activity.jpa.specification.activity.ActivitySpecification;
import com.kernotec.farm.activity.rest.dto.request.activity.ActivityFindAllFilterRequest;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Activity> findAllWithFilters(ActivityFindAllFilterRequest filterRequest,
        Pageable pageable)
    {
        return repository.findAll(
            ActivitySpecification.builder()
                .includeOnlyUserActivities(Boolean.TRUE)
                .withSocialNetworkId(filterRequest.getSocialNetworkId())
                .withDeviceId(filterRequest.getDeviceId())
                .withAccountIdList(filterRequest.getAccountIds()), pageable
        );
    }

    public Page<Activity> findAllByKeyword(String keyword, Pageable pageable) {
        return repository.findAll(
            ActivitySpecification.builder()
                .includeOnlyUserActivities(Boolean.TRUE)
                .withKeyword(keyword), pageable
        );
    }
}
