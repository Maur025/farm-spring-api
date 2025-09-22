package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.entity.AccountGroup;
import com.kernotec.farm.activity.jpa.entity.Group;
import com.kernotec.farm.parametric.jpa.entity.GroupState;
import com.kernotec.farm.parametric.jpa.entity.Region;
import com.kernotec.farm.parametric.jpa.enums.GroupStateCodeEnum;
import com.kernotec.farm.report.rest.dto.request.ActivitySummaryByAccountRequest;
import com.kernotec.farm.report.rest.dto.response.account.AccountSummaryTableResponse;
import com.kernotec.farm.report.rest.dto.response.account.GroupRegionSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.GroupSummaryResponse;
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
public class GroupSummaryService {

    private final EntityManager entityManager;
    private final CommonPredicateToSummary commonPredicateToSummary;

    private CriteriaBuilder cb;

    @PostConstruct
    private void init() {
        this.cb = entityManager.getCriteriaBuilder();
    }

    public GroupSummaryResponse getGroupSummary(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest)
    {
        return GroupSummaryResponse.builder()
            .totalGroups(getTotalGroups(accountId, filterRequest))
            .totalsByRegion(getTotalGroupsByRegion(accountId, filterRequest))
            .groups(getGroupsOfResult(accountId, filterRequest))
            .build();
    }


    private Long getTotalGroups(UUID accountId, ActivitySummaryByAccountRequest filterRequest) {
        CriteriaQuery<Long> queryOfTotalGroups = cb.createQuery(Long.class);
        Root<Account> accountRoot = queryOfTotalGroups.from(Account.class);

        Join<Account, AccountGroup> accountGroupJoin = accountRoot.join(
            "accountGroups", JoinType.INNER);

        queryOfTotalGroups.select(cb.countDistinct(accountGroupJoin.get("id")));

        queryOfTotalGroups.where(cb.and(
            getPredicatesOfGroups(accountRoot, accountId, filterRequest, accountGroupJoin).toArray(
                Predicate[]::new)));

        return entityManager.createQuery(queryOfTotalGroups)
            .getSingleResult();
    }

    private List<AccountSummaryTableResponse> getGroupsOfResult(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest)
    {
        CriteriaQuery<AccountSummaryTableResponse> queryGroupsOfResult = cb.createQuery(
            AccountSummaryTableResponse.class);
        Root<Account> accountRoot = queryGroupsOfResult.from(Account.class);

        Join<Account, AccountGroup> accountGroupJoin = accountRoot.join(
            "accountGroups", JoinType.INNER);
        Join<AccountGroup, Group> groupJoin = accountGroupJoin.join("group", JoinType.LEFT);
        Join<Group, Region> regionJoin = groupJoin.join("region", JoinType.LEFT);

        queryGroupsOfResult.select(cb.construct(
            AccountSummaryTableResponse.class,
            /* id -> groupId */
            groupJoin.get("id"),
            /* name -> groupName */
            groupJoin.get("name"),
            /* detail -> regionName */
            regionJoin.get("name")
        ));

        queryGroupsOfResult.where(cb.and(
            getPredicatesOfGroups(accountRoot, accountId, filterRequest, accountGroupJoin).toArray(
                Predicate[]::new)));

        return entityManager.createQuery(queryGroupsOfResult)
            .getResultList();
    }

    private List<GroupRegionSummaryResponse> getTotalGroupsByRegion(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest)
    {
        CriteriaQuery<GroupRegionSummaryResponse> queryGroupsByRegion = cb.createQuery(
            GroupRegionSummaryResponse.class);
        Root<Account> accountRoot = queryGroupsByRegion.from(Account.class);

        Join<Account, AccountGroup> accountGroupJoin = accountRoot.join(
            "accountGroups", JoinType.INNER);

        Join<AccountGroup, Group> groupJoin = accountGroupJoin.join("group", JoinType.INNER);
        Join<Group, Region> regionJoin = groupJoin.join("region", JoinType.INNER);

        queryGroupsByRegion.select(cb.construct(
            GroupRegionSummaryResponse.class,
            /* regionName */
            regionJoin.get("name"),
            /* regionId */
            regionJoin.get("id"),
            /* totalGroupsByRegion */
            cb.countDistinct(accountGroupJoin)
        ));

        queryGroupsByRegion.where(cb.and(
            getPredicatesOfGroups(accountRoot, accountId, filterRequest, accountGroupJoin).toArray(
                Predicate[]::new)));

        queryGroupsByRegion.groupBy(regionJoin.get("id"), regionJoin.get("name"));

        return entityManager.createQuery(queryGroupsByRegion)
            .getResultList();
    }

    private List<Predicate> getPredicatesOfGroups(Root<Account> accountRoot, UUID accountId,
        ActivitySummaryByAccountRequest filterRequest, Join<Account, AccountGroup> accountGroupJoin)
    {
        Join<AccountGroup, GroupState> groupStateJoin = accountGroupJoin.join(
            "groupState", JoinType.INNER);

        List<Predicate> predicateList = commonPredicateToSummary.getCommonAccountPredicates(
            accountRoot, accountId, filterRequest.getSocialNetworkId());

        commonPredicateToSummary.addTimeLapsePredicate(
            filterRequest, predicateList, accountGroupJoin.get("joinedAt"));

        if (groupStateJoin != null) {
            predicateList.add(
                cb.equal(groupStateJoin.get("code"), GroupStateCodeEnum.JOINED.toString()));
        }

        return predicateList;
    }
}
