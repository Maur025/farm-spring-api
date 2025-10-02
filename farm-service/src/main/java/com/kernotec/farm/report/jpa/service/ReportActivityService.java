package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.repository.ActivityRepository;
import com.kernotec.farm.activity.jpa.specification.activity.ActivitySpecification;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.parametric.jpa.enums.ActivityTypeCodeEnum;
import com.kernotec.farm.report.rest.dto.request.ReportActivityRequest;
import com.kernotec.farm.report.rest.dto.response.activity.ActivityTypeTotalResponse;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportActivityService {

    private final ActivityRepository repository;
    private final EntityManager entityManager;
    private CriteriaBuilder cb;

    @PostConstruct
    public void init() {
        cb = entityManager.getCriteriaBuilder();
    }

    public Page<Activity> findAllWithFilters(ReportActivityRequest filterRequest, Pageable pageable)
    {
        return repository.findAll(
            ActivitySpecification.builder()
                .includeOnlyUserActivities(Boolean.TRUE)
                .withZoneId(filterRequest.getZoneId())
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

    public List<Activity> findAllWithFiltersNoPaginated(ReportActivityRequest filterRequest) {
        return repository.findAll(ActivitySpecification.builder()
            .includeOnlyUserActivities(Boolean.TRUE)
            .withZoneId(filterRequest.getZoneId())
            .withUserAuthId(filterRequest.getUserAuthId())
            .withSocialNetworkId(filterRequest.getSocialNetworkId())
            .withDeviceId(filterRequest.getDeviceId())
            .withFarmId(filterRequest.getFarmId())
            .withActivityTypeId(filterRequest.getActivityTypeId())
            .withAccountId(filterRequest.getAccountId())
            .withSimpleDate(filterRequest.getSimpleDate())
            .withDateRange(filterRequest.getFromDate(), filterRequest.getToDate())
            .withMonthDate(filterRequest.getMonthDate())
            .withYearDate(filterRequest.getYearDate()));
    }

    public ActivityTypeTotalResponse getTotalActivitiesByType(ReportActivityRequest filterRequest) {
        CriteriaQuery<ActivityTypeTotalResponse> totalActivitiesQuery = cb.createQuery(
            ActivityTypeTotalResponse.class);
        Root<Activity> activityRoot = totalActivitiesQuery.from(Activity.class);
        Join<Activity, ActivityType> activityTypeJoin = activityRoot.join(
            "activityType", JoinType.INNER);

        totalActivitiesQuery.select(cb.construct(
            ActivityTypeTotalResponse.class,
            // totalActivities;
            cb.countDistinct(activityRoot.get("id")),
            // totalComments;
            cb.coalesce(
                cb.sum(cb.<Long>selectCase()
                    .when(
                        cb.equal(
                            activityTypeJoin.get("code"),
                            ActivityTypeCodeEnum.COMENTARIO.toString()
                        ), 1L
                    )
                    .otherwise(0L)), 0L
            ),
            // totalGroups;
            cb.coalesce(
                cb.sum(cb.<Long>selectCase()
                    .when(
                        cb.equal(
                            activityTypeJoin.get("code"), ActivityTypeCodeEnum.GRUPO.toString()), 1L
                    )
                    .otherwise(0L)), 0L
            ),
            // totalFriends;
            cb.coalesce(
                cb.sum(cb.<Long>selectCase()
                    .when(
                        cb.equal(
                            activityTypeJoin.get("code"), ActivityTypeCodeEnum.AMISTAD.toString()),
                        1L
                    )
                    .otherwise(0L)), 0L
            ),
            // totalPageFollows;
            cb.coalesce(
                cb.sum(cb.<Long>selectCase()
                    .when(
                        cb.equal(
                            activityTypeJoin.get("code"),
                            ActivityTypeCodeEnum.FOLLOW.toString()
                        ), 1L
                    )
                    .otherwise(0L)), 0L
            ),
            // totalReactions;
            cb.coalesce(
                cb.sum(cb.<Long>selectCase()
                    .when(
                        cb.equal(
                            activityTypeJoin.get("code"),
                            ActivityTypeCodeEnum.REACCION.toString()
                        ), 1L
                    )
                    .otherwise(0L)), 0L
            ),
            // totalPublications;
            cb.coalesce(
                cb.sum(cb.<Long>selectCase()
                    .when(
                        cb.equal(
                            activityTypeJoin.get("code"),
                            ActivityTypeCodeEnum.PUBLICACION.toString()
                        ), 1L
                    )
                    .otherwise(0L)), 0L
            ),
            // totalTiktokProfileFollows;
            cb.coalesce(
                cb.sum(cb.<Long>selectCase()
                    .when(
                        cb.equal(
                            activityTypeJoin.get("code"),
                            ActivityTypeCodeEnum.FOLLOW_TIKTOK.toString()
                        ), 1L
                    )
                    .otherwise(0L)), 0L
            )

        ));

        totalActivitiesQuery.where(ActivitySpecification.builder()
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
            .withYearDate(filterRequest.getYearDate())
            .toPredicate(activityRoot, totalActivitiesQuery, cb));

        return entityManager.createQuery(totalActivitiesQuery)
            .getSingleResult();
    }
}
