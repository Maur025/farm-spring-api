package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.specification.activity.ActivitySpecification;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.jpa.entity.Farm;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.parametric.jpa.enums.ActivityTypeCodeEnum;
import com.kernotec.farm.report.rest.dto.request.ReportDashboardRequest;
import com.kernotec.farm.report.rest.dto.response.farm.FarmReportTotalResponse;
import com.kernotec.farm.util.CommonSpecification;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportDashboardService {

    private final EntityManager entityManager;
    private CriteriaBuilder cb;

    @PostConstruct
    public void init() {
        cb = entityManager.getCriteriaBuilder();
    }

    public List<FarmReportTotalResponse> findReportTotalFarms(ReportDashboardRequest filterRequest)
    {
        CriteriaQuery<FarmReportTotalResponse> totalActivitiesByFarmQuery = cb.createQuery(
            FarmReportTotalResponse.class);

        Root<Activity> activityRoot = totalActivitiesByFarmQuery.from(Activity.class);
        Join<Activity, ActivityType> activityTypeJoin = activityRoot.join(
            "activityType", JoinType.INNER);
        Join<Activity, Account> accountJoin = activityRoot.join("account", JoinType.INNER);
        Join<Account, Device> deviceJoin = accountJoin.join("devices", JoinType.INNER);
        Join<Device, Farm> farmJoin = deviceJoin.join("farm", JoinType.INNER);

        totalActivitiesByFarmQuery.select(cb.construct(
            FarmReportTotalResponse.class,
            // farmId
            farmJoin.get("id"),
            // name
            farmJoin.get("name"),
            //totalActivities
            cb.countDistinct(activityRoot.get("id")),
            //comments
            CommonSpecification.getTotalBySumCase(
                cb, activityTypeJoin.get("code"),
                ActivityTypeCodeEnum.COMENTARIO.toString()
            ),
            // group
            CommonSpecification.getTotalBySumCase(
                cb, activityTypeJoin.get("code"),
                ActivityTypeCodeEnum.GRUPO.toString()
            ),
            // friends
            CommonSpecification.getTotalBySumCase(
                cb, activityTypeJoin.get("code"),
                ActivityTypeCodeEnum.AMISTAD.toString()
            ),
            // Follows
            CommonSpecification.getTotalBySumCase(
                cb, activityTypeJoin.get("code"),
                ActivityTypeCodeEnum.FOLLOW.toString()
            ),
            // Reactions
            CommonSpecification.getTotalBySumCase(
                cb, activityTypeJoin.get("code"),
                ActivityTypeCodeEnum.REACCION.toString()
            ),
            // publications
            CommonSpecification.getTotalBySumCase(
                cb, activityTypeJoin.get("code"),
                ActivityTypeCodeEnum.PUBLICACION.toString()
            ),
            // tiktokFollows
            CommonSpecification.getTotalBySumCase(
                cb, activityTypeJoin.get("code"),
                ActivityTypeCodeEnum.FOLLOW_TIKTOK.toString()
            )
        ));

        totalActivitiesByFarmQuery.groupBy(farmJoin.get("id"), farmJoin.get("name"));

        totalActivitiesByFarmQuery.where(ActivitySpecification.builder()
            .includeOnlyUserActivities(true)
            .withSocialNetworkId(filterRequest.getSocialNetworkId())
            .withUserAuthId(filterRequest.getAuthUserId())
            .withMonthDate(filterRequest.getMonthDate())
            .withZoneId(filterRequest.getZoneId())
            .toPredicate(activityRoot, totalActivitiesByFarmQuery, cb));

        return entityManager.createQuery(totalActivitiesByFarmQuery)
            .getResultList();
    }
}
