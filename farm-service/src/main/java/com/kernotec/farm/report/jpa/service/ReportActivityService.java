package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.repository.ActivityRepository;
import com.kernotec.farm.activity.jpa.specification.activity.ActivitySpecification;
import com.kernotec.farm.report.rest.dto.request.ReportActivityRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportActivityService {

    private final ActivityRepository repository;

    public Page<Activity> findAllWithFilters(ReportActivityRequest filterRequest, Pageable pageable)
    {
        return repository.findAll(
            ActivitySpecification.builder()
                .includeOnlyUserActivities(Boolean.TRUE)
                .withUserAuthId(filterRequest.getUserAuthId())
                .withSocialNetworkId(filterRequest.getSocialNetworkId())
                .withDeviceId(filterRequest.getDeviceId())
                .withFarmId(filterRequest.getFarmId())
                .withActivityTypeId(filterRequest.getActivityTypeId())
                .withAccountId(filterRequest.getAccountId())
                .withSimpleDate(filterRequest.getSimpleDate())
                .withDateRange(filterRequest.getFromDate(), filterRequest.getToDate())
                .withMonthDate(filterRequest.getMonthDate())
                .withYearDate(filterRequest.getYearDate()), pageable
        );
    }
}
