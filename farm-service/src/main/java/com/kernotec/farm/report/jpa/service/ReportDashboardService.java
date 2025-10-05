package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.account.jpa.specification.account.AccountSpecification;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.specification.activity.ActivitySpecification;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.jpa.entity.Farm;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.parametric.jpa.entity.SocialNetwork;
import com.kernotec.farm.parametric.jpa.enums.ActivityTypeCodeEnum;
import com.kernotec.farm.report.rest.dto.request.ReportDashboardRequest;
import com.kernotec.farm.report.rest.dto.response.farm.FarmReportTotalResponse;
import com.kernotec.farm.report.rest.dto.response.social.network.ReportActivitySocialNetworkResponse;
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

    public List<ReportActivitySocialNetworkResponse> findActivitiesGroupBySocialNetwork(
        ReportDashboardRequest filterRequest)
    {
        CriteriaQuery<ReportActivitySocialNetworkResponse> activitySocialNetworkQuery = cb.createQuery(
            ReportActivitySocialNetworkResponse.class);
        Root<Activity> activityRoot = activitySocialNetworkQuery.from(Activity.class);
        Join<Activity, Account> accountJoin = activityRoot.join("account", JoinType.INNER);
        Join<Account, SocialNetwork> socialNetworkJoin = accountJoin.join(
            "socialNetwork", JoinType.INNER);

        activitySocialNetworkQuery.select(cb.construct(
            ReportActivitySocialNetworkResponse.class,
            // socialNetworkId
            socialNetworkJoin.get("id"),
            // socialNetworkName
            socialNetworkJoin.get("name"),
            // socialNetworkCode
            socialNetworkJoin.get("code"),
            // socialNetworkIcon
            socialNetworkJoin.get("icon"),
            // socialNetworkColor
            socialNetworkJoin.get("color"),
            // totalActivities
            cb.countDistinct(activityRoot.get("id"))
        ));

        activitySocialNetworkQuery.groupBy(
            socialNetworkJoin.get("id"), socialNetworkJoin.get("name"),
            socialNetworkJoin.get("code"), socialNetworkJoin.get("icon"),
            socialNetworkJoin.get("color")
        );

        activitySocialNetworkQuery.where(ActivitySpecification.builder()
            .includeOnlyUserActivities(true)
            .withSocialNetworkId(filterRequest.getSocialNetworkId())
            .withUserAuthId(filterRequest.getAuthUserId())
            .withMonthDate(filterRequest.getMonthDate())
            .withZoneId(filterRequest.getZoneId())
            .toPredicate(activityRoot, activitySocialNetworkQuery, cb));

        return entityManager.createQuery(activitySocialNetworkQuery)
            .getResultList();
    }

    public Long countActivitiesWithFilters(ReportDashboardRequest filterRequest) {
        CriteriaQuery<Long> countActivitiesQuery = cb.createQuery(Long.class);
        Root<Activity> activityRoot = countActivitiesQuery.from(Activity.class);

        countActivitiesQuery.select(cb.countDistinct(activityRoot.get("id")));

        countActivitiesQuery.where(ActivitySpecification.builder()
            .includeOnlyUserActivities(true)
            .withSocialNetworkId(filterRequest.getSocialNetworkId())
            .withUserAuthId(filterRequest.getAuthUserId())
            .withMonthDate(filterRequest.getMonthDate())
            .withZoneId(filterRequest.getZoneId())
            .toPredicate(activityRoot, countActivitiesQuery, cb));

        return entityManager.createQuery(countActivitiesQuery)
            .getSingleResult();
    }

    public Long countTotalDevices() {
        CriteriaQuery<Long> countDevicesQuery = cb.createQuery(Long.class);
        Root<Device> deviceRoot = countDevicesQuery.from(Device.class);

        countDevicesQuery.select(cb.countDistinct(deviceRoot.get("id")));

        return entityManager.createQuery(countDevicesQuery)
            .getSingleResult();
    }

    public Long countTotalFarms() {
        CriteriaQuery<Long> countFarmsQuery = cb.createQuery(Long.class);
        Root<Farm> farmRoot = countFarmsQuery.from(Farm.class);

        countFarmsQuery.select(cb.countDistinct(farmRoot.get("id")));

        return entityManager.createQuery(countFarmsQuery)
            .getSingleResult();
    }

    public Long countTotalAccounts(ReportDashboardRequest filterRequest) {
        CriteriaQuery<Long> countAccountsQuery = cb.createQuery(Long.class);
        Root<Account> accountRoot = countAccountsQuery.from(Account.class);

        countAccountsQuery.select(cb.countDistinct(accountRoot.get("id")));

        countAccountsQuery.where(AccountSpecification.builder()
            .withSocialNetworkId(filterRequest.getSocialNetworkId())
            .withAccountType(AccountTypeEnum.INTERNAL)
            .toPredicate(accountRoot, countAccountsQuery, cb));

        return entityManager.createQuery(countAccountsQuery)
            .getSingleResult();
    }
}
