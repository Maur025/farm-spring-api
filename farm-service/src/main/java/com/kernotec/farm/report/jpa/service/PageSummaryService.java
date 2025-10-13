package com.kernotec.farm.report.jpa.service;

import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.entity.AccountFollowProfile;
import com.kernotec.farm.account.jpa.enums.AccountFollowProfileStateEnum;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.entity.Follow;
import com.kernotec.farm.activity.jpa.entity.Profile;
import com.kernotec.farm.activity.jpa.specification.activity.ActivitySpecification;
import com.kernotec.farm.parametric.jpa.entity.Region;
import com.kernotec.farm.report.rest.dto.request.ActivitySummaryByAccountRequest;
import com.kernotec.farm.report.rest.dto.request.ReportDashboardRequest;
import com.kernotec.farm.report.rest.dto.response.account.AccountSummaryTableResponse;
import com.kernotec.farm.report.rest.dto.response.account.PageRegionSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.PageSummaryResponse;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PageSummaryService {

    private final EntityManager entityManager;
    private final CommonPredicateToSummary commonPredicateToSummary;

    private CriteriaBuilder cb;

    @PostConstruct
    private void init() {
        this.cb = entityManager.getCriteriaBuilder();
    }

    public PageSummaryResponse getPageSummary(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest)
    {
        Long totalPages = getTotalPages(accountId, filterRequest);

        return PageSummaryResponse.builder()
            .totalPages(totalPages)
            .totalsByRegion(getTotalPagesByRegion(accountId, filterRequest))
            .profiles(getProfilesOfResult(accountId, filterRequest, totalPages))
            .build();
    }

    private Long getTotalPages(UUID accountId, ActivitySummaryByAccountRequest filterRequest) {
        CriteriaQuery<Long> queryOfTotalPages = cb.createQuery(Long.class);
        Root<Account> accountRoot = queryOfTotalPages.from(Account.class);

        Join<Account, AccountFollowProfile> accountFollowProfileJoin = accountRoot.join(
            "accountFollowProfiles", JoinType.INNER);

        queryOfTotalPages.select(cb.countDistinct(accountFollowProfileJoin.get("id")));

        queryOfTotalPages.where(cb.and(getPredicatesOfPages(
            accountRoot, accountId, filterRequest,
            accountFollowProfileJoin
        ).toArray(Predicate[]::new)));

        return entityManager.createQuery(queryOfTotalPages)
            .getSingleResult();
    }

    private PageResponse<AccountSummaryTableResponse> getProfilesOfResult(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest, Long totalPages)
    {
        CriteriaQuery<AccountSummaryTableResponse> getPagesQuery = cb.createQuery(
            AccountSummaryTableResponse.class);
        Root<Account> accountRoot = getPagesQuery.from(Account.class);

        Join<Account, AccountFollowProfile> accountFollowProfileJoin = accountRoot.join(
            "accountFollowProfiles", JoinType.INNER);
        Join<AccountFollowProfile, Profile> profileJoin = accountFollowProfileJoin.join(
            "profile", JoinType.INNER);
        Join<Profile, Region> regionJoin = profileJoin.join("region", JoinType.LEFT);

        getPagesQuery.select(cb.construct(
            AccountSummaryTableResponse.class,
            /* profileId */
            profileJoin.get("id"),
            /* profileName */
            profileJoin.get("name"),
            /* regionName */
            regionJoin.get("name")
        ));

        getPagesQuery.where(cb.and(getPredicatesOfPages(
            accountRoot, accountId, filterRequest,
            accountFollowProfileJoin
        ).toArray(Predicate[]::new)));

        if (filterRequest.getPageable() == null) {
            return PageResponse.<AccountSummaryTableResponse>builder()
                .data(entityManager.createQuery(getPagesQuery)
                    .getResultList())
                .build();
        }

        return commonPredicateToSummary.getResultTableWithPagination(
            getPagesQuery, filterRequest.getPageable(), totalPages);
    }

    private List<PageRegionSummaryResponse> getTotalPagesByRegion(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest)
    {
        CriteriaQuery<PageRegionSummaryResponse> queryPagesByRegion = cb.createQuery(
            PageRegionSummaryResponse.class);
        Root<Account> accountRoot = queryPagesByRegion.from(Account.class);

        Join<Account, AccountFollowProfile> accountFollowProfileJoin = accountRoot.join(
            "accountFollowProfiles", JoinType.INNER);
        Join<AccountFollowProfile, Profile> profileJoin = accountFollowProfileJoin.join(
            "profile", JoinType.INNER);
        Join<Profile, Region> regionJoin = profileJoin.join("region", JoinType.INNER);

        queryPagesByRegion.select(cb.construct(
            PageRegionSummaryResponse.class,
            /* regionName */
            regionJoin.get("name"),
            /* regionId */
            regionJoin.get("id"),
            /* totalPagesByRegion */
            cb.countDistinct(accountFollowProfileJoin.get("id"))
        ));

        queryPagesByRegion.where(cb.and(getPredicatesOfPages(
            accountRoot, accountId, filterRequest,
            accountFollowProfileJoin
        ).toArray(Predicate[]::new)));

        queryPagesByRegion.groupBy(regionJoin.get("id"), regionJoin.get("name"));

        return entityManager.createQuery(queryPagesByRegion)
            .getResultList();
    }

    private List<Predicate> getPredicatesOfPages(Root<Account> accountRoot, UUID accountId,
        ActivitySummaryByAccountRequest filterRequest, Join<?, ?> accountFollowProfileJoin)
    {
        List<Predicate> predicateList = commonPredicateToSummary.getCommonAccountPredicates(
            accountRoot, accountId, filterRequest.getSocialNetworkId());

        commonPredicateToSummary.addTimeLapsePredicate(
            filterRequest, predicateList, accountFollowProfileJoin.get("followedAt"));

        cb.equal(
            accountFollowProfileJoin.get("followState"), AccountFollowProfileStateEnum.FOLLOWING);

        return predicateList;
    }

    public List<PageRegionSummaryResponse> getTotalActivitiesPageGroupedByRegion(
        ReportDashboardRequest filterRequest)
    {
        CriteriaQuery<PageRegionSummaryResponse> pageRegionQuery = cb.createQuery(
            PageRegionSummaryResponse.class);

        Root<Activity> activityRoot = pageRegionQuery.from(Activity.class);
        Join<Activity, Follow> followJoin = activityRoot.join("follows", JoinType.INNER);
        Join<Follow, Profile> profileJoin = followJoin.join("profile", JoinType.LEFT);
        Join<Profile, Region> regionJoin = profileJoin.join("region", JoinType.LEFT);

        pageRegionQuery.select(cb.construct(
            PageRegionSummaryResponse.class,
            // regionName
            regionJoin.get("name"),
            // regionId
            regionJoin.get("id"),
            // totalPagesByRegion
            cb.countDistinct(followJoin.get("id"))
        ));

        pageRegionQuery.groupBy(regionJoin.get("name"), regionJoin.get("id"));

        pageRegionQuery.where(ActivitySpecification.builder()
            .includeOnlyUserActivities(true)
            .withSocialNetworkId(filterRequest.getSocialNetworkId())
            .withUserAuthId(filterRequest.getAuthUserId())
            .withMonthDate(filterRequest.getMonthDate())
            .withZoneId(filterRequest.getZoneId())
            .toPredicate(activityRoot, pageRegionQuery, cb));

        return entityManager.createQuery(pageRegionQuery)
            .getResultList();
    }

    public Long countAllProfiles() {
        CriteriaQuery<Long> countProfilesQuery = cb.createQuery(Long.class);
        Root<Profile> profileRoot = countProfilesQuery.from(Profile.class);

        countProfilesQuery.select(cb.countDistinct(profileRoot.get("id")));

        return entityManager.createQuery(countProfilesQuery)
            .getSingleResult();
    }

    public List<PageRegionSummaryResponse> getProfilesCountGroupByRegion() {
        CriteriaQuery<PageRegionSummaryResponse> countProfilesByRegionQuery = cb.createQuery(
            PageRegionSummaryResponse.class);
        Root<Profile> profileRoot = countProfilesByRegionQuery.from(Profile.class);
        Join<Profile, Region> regionJoin = profileRoot.join("region", JoinType.INNER);

        countProfilesByRegionQuery.select(cb.construct(
            PageRegionSummaryResponse.class,
            // regionName
            regionJoin.get("name"),
            // regionId
            regionJoin.get("id"),
            // totalPagesByRegion
            cb.countDistinct(profileRoot.get("id"))
        ));

        countProfilesByRegionQuery.groupBy(regionJoin.get("name"), regionJoin.get("id"));

        return entityManager.createQuery(countProfilesByRegionQuery)
            .getResultList();
    }
}
