package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.account.command.account.AccountGetDtoCmd;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.entity.AccountGroup;
import com.kernotec.farm.account.jpa.entity.Friend;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.activity.jpa.entity.Group;
import com.kernotec.farm.parametric.jpa.entity.FriendState;
import com.kernotec.farm.parametric.jpa.entity.GroupState;
import com.kernotec.farm.parametric.jpa.entity.Region;
import com.kernotec.farm.parametric.jpa.enums.FriendStateCodeEnum;
import com.kernotec.farm.parametric.jpa.enums.GroupStateCodeEnum;
import com.kernotec.farm.report.rest.dto.response.account.ActivitySummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.FriendSummaryResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountReportService {

    private final EntityManager entityManager;
    private final AccountGetDtoCmd accountGetDtoCmd;
    private CriteriaBuilder cb;

    @PostConstruct
    private void init() {
        this.cb = entityManager.getCriteriaBuilder();
    }

    public ActivitySummaryResponse getActivitySummaryByAccountId(UUID accountId,
        UUID socialNetworkId)
    {
        AccountDto accountDto = accountGetDtoCmd.withRequest(AccountGetDtoCmd.Request.builder()
                .accountId(accountId)
                .build())
            .execute();

        FriendSummaryResponse friendSummary = getFriendSummary(accountId, socialNetworkId);

        return ActivitySummaryResponse.builder()
            .accountId(accountId)
            .accountName(accountDto.getUsername())
            .socialNetworkId(socialNetworkId)
            .socialNetworkName(accountDto.getSocialNetwork()
                .getName())
            .friendSummary(friendSummary)
            .groupSummary(GroupSummaryResponse.builder()
                .totalGroups(getTotalGroups(accountId, socialNetworkId))
                .totalsByRegion(getTotalGroupsByRegion(accountId, socialNetworkId))
                .build())
            .build();
    }

    private FriendSummaryResponse getFriendSummary(UUID accountId, UUID socialNetworkId) {
        CriteriaQuery<FriendSummaryResponse> query = cb.createQuery(FriendSummaryResponse.class);
        Root<Account> root = query.from(Account.class);

        Join<Account, Friend> friendJoin = root.join("friends", JoinType.INNER);
        Join<Friend, FriendState> friendStateJoin = friendJoin.join("friendState", JoinType.INNER);
        Join<Friend, Account> targetAccountJoin = friendJoin.join("friendAccount", JoinType.INNER);

        query.select(cb.construct(
            FriendSummaryResponse.class,
            /* totalFriends */
            cb.countDistinct(friendJoin.get("id")),
            /* totalInternalFriends */
            cb.coalesce(
                cb.sum(cb.<Long>selectCase()
                    .when(
                        cb.and(
                            cb.equal(friendJoin.get("accountId"), accountId),
                            cb.equal(targetAccountJoin.get("type"), AccountTypeEnum.INTERNAL)
                        ), 1L
                    )
                    .otherwise(0L)), 0L
            ),
            /* totalExternalFriends */
            cb.coalesce(
                cb.sum(cb.<Long>selectCase()
                    .when(
                        cb.and(
                            cb.equal(friendJoin.get("accountId"), accountId),
                            cb.equal(targetAccountJoin.get("type"), AccountTypeEnum.EXTERNAL)
                        ), 1L
                    )
                    .otherwise(0L)), 0L
            )
        ));

        List<Predicate> predicateList = getCommonPredicates(root, accountId, socialNetworkId);

        if (friendStateJoin != null) {
            predicateList.add(
                cb.equal(friendStateJoin.get("code"), FriendStateCodeEnum.ACTIVE.toString()));
        }

        query.where(cb.and(predicateList.toArray(Predicate[]::new)));

        return entityManager.createQuery(query)
            .getSingleResult();
    }

    private Long getTotalGroups(UUID accountId, UUID socialNetworkId) {
        CriteriaQuery<Long> queryOfTotal = cb.createQuery(Long.class);
        Root<Account> accountRoot = queryOfTotal.from(Account.class);

        Join<Account, AccountGroup> accountGroupJoin = accountRoot.join(
            "accountGroups", JoinType.INNER);
        Join<AccountGroup, GroupState> groupStateJoin = accountGroupJoin.join(
            "groupState", JoinType.INNER);

        queryOfTotal.select(cb.countDistinct(accountGroupJoin.get("id")));

        queryOfTotal.where(cb.and(
            getPredicatesOfGroups(accountRoot, accountId, socialNetworkId, groupStateJoin).toArray(
                Predicate[]::new)));

        return entityManager.createQuery(queryOfTotal)
            .getSingleResult();
    }

    private List<GroupRegionSummaryResponse> getTotalGroupsByRegion(UUID accountId,
        UUID socialNetworkId)
    {
        CriteriaQuery<GroupRegionSummaryResponse> queryOfDetail = cb.createQuery(
            GroupRegionSummaryResponse.class);
        Root<Account> accountRoot = queryOfDetail.from(Account.class);

        Join<Account, AccountGroup> accountGroupJoin = accountRoot.join(
            "accountGroups", JoinType.INNER);

        Join<AccountGroup, Group> groupJoin = accountGroupJoin.join("group", JoinType.INNER);
        Join<Group, Region> regionJoin = groupJoin.join("region", JoinType.INNER);

        Join<AccountGroup, GroupState> groupStateJoin = accountGroupJoin.join(
            "groupState", JoinType.INNER);

        queryOfDetail.select(cb.construct(
            GroupRegionSummaryResponse.class,
            /* regionName */
            regionJoin.get("name"),
            /* regionId */
            regionJoin.get("id"),
            /* totalGroupsByRegion */
            cb.countDistinct(accountGroupJoin)
        ));

        queryOfDetail.where(cb.and(
            getPredicatesOfGroups(accountRoot, accountId, socialNetworkId, groupStateJoin).toArray(
                Predicate[]::new)));

        queryOfDetail.groupBy(regionJoin.get("id"), regionJoin.get("name"));

        return entityManager.createQuery(queryOfDetail)
            .getResultList();
    }

    private List<Predicate> getPredicatesOfGroups(Root<Account> accountRoot, UUID accountId,
        UUID socialNetworkId, Join<?, ?> groupStateJoin)
    {
        List<Predicate> predicateList = getCommonPredicates(
            accountRoot, accountId, socialNetworkId);

        if (groupStateJoin != null) {
            predicateList.add(
                cb.equal(groupStateJoin.get("code"), GroupStateCodeEnum.JOINED.toString()));
        }

        return predicateList;
    }

    private List<Predicate> getCommonPredicates(Root<Account> root, UUID accountId,
        UUID socialNetworkId)
    {
        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(cb.equal(root.get("id"), accountId));
        predicateList.add(cb.equal(root.get("socialNetworkId"), socialNetworkId));

        return predicateList;
    }
}
