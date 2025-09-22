package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.entity.AccountFollowProfile;
import com.kernotec.farm.account.jpa.enums.AccountFollowProfileStateEnum;
import com.kernotec.farm.activity.jpa.entity.Profile;
import com.kernotec.farm.parametric.jpa.entity.Region;
import com.kernotec.farm.report.rest.dto.request.ActivitySummaryByAccountRequest;
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
        return PageSummaryResponse.builder()
            .totalPages(getTotalPages(accountId, filterRequest.getSocialNetworkId()))
            .totalsByRegion(getTotalPagesByRegion(accountId, filterRequest.getSocialNetworkId()))
            .profiles(getProfilesOfResult(accountId, filterRequest.getSocialNetworkId()))
            .build();
    }

    private Long getTotalPages(UUID accountId, UUID socialNetworkId) {
        CriteriaQuery<Long> queryOfTotalPages = cb.createQuery(Long.class);
        Root<Account> accountRoot = queryOfTotalPages.from(Account.class);

        Join<Account, AccountFollowProfile> accountFollowProfileJoin = accountRoot.join(
            "accountFollowProfiles", JoinType.INNER);

        queryOfTotalPages.select(cb.countDistinct(accountFollowProfileJoin.get("id")));

        queryOfTotalPages.where(cb.and(getPredicatesOfPages(
            accountRoot, accountId, socialNetworkId,
            accountFollowProfileJoin
        ).toArray(Predicate[]::new)));

        return entityManager.createQuery(queryOfTotalPages)
            .getSingleResult();
    }

    private List<AccountSummaryTableResponse> getProfilesOfResult(UUID accountId, UUID socialNetworkId)
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
            accountRoot, accountId, socialNetworkId,
            accountFollowProfileJoin
        ).toArray(Predicate[]::new)));

        return entityManager.createQuery(getPagesQuery)
            .getResultList();
    }

    private List<PageRegionSummaryResponse> getTotalPagesByRegion(UUID accountId,
        UUID socialNetworkId)
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

        queryPagesByRegion.where(cb.and(
            getPredicatesOfPages(
                accountRoot, accountId, socialNetworkId, accountFollowProfileJoin).toArray(
                Predicate[]::new)));

        queryPagesByRegion.groupBy(regionJoin.get("id"), regionJoin.get("name"));

        return entityManager.createQuery(queryPagesByRegion)
            .getResultList();
    }

    private List<Predicate> getPredicatesOfPages(Root<Account> accountRoot, UUID accountId,
        UUID socialNetworkId, Join<?, ?> accountFollowProfileJoin)
    {
        List<Predicate> predicateList = commonPredicateToSummary.getCommonAccountPredicates(
            accountRoot, accountId, socialNetworkId);

        cb.equal(
            accountFollowProfileJoin.get("followState"), AccountFollowProfileStateEnum.FOLLOWING);

        return predicateList;
    }
}
