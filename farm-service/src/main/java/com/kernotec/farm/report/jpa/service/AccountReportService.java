package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.entity.AccountGroup;
import com.kernotec.farm.account.jpa.entity.Friend;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.parametric.jpa.entity.FriendState;
import com.kernotec.farm.parametric.jpa.entity.GroupState;
import com.kernotec.farm.parametric.jpa.enums.FriendStateCodeEnum;
import com.kernotec.farm.parametric.jpa.enums.GroupStateCodeEnum;
import com.kernotec.farm.report.rest.dto.response.account.ActivitySummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.FriendSummaryResponse;
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
    private CriteriaBuilder cb;

    @PostConstruct
    private void init() {
        this.cb = entityManager.getCriteriaBuilder();
    }

    public ActivitySummaryResponse getActivitySummaryByAccountId(UUID accountId,
        UUID socialNetworkId)
    {

        FriendSummaryResponse friendSummary = getFriendSummary(accountId, socialNetworkId);
        GroupSummaryResponse groupSummary = getGroupSummary(accountId, socialNetworkId);

        return ActivitySummaryResponse.builder()
            .friendSummary(friendSummary)
            .groupSummary(groupSummary)
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

    private GroupSummaryResponse getGroupSummary(UUID accountId, UUID socialNetworkId) {
        CriteriaQuery<GroupSummaryResponse> query = cb.createQuery(GroupSummaryResponse.class);
        Root<Account> accountRoot = query.from(Account.class);

        Join<Account, AccountGroup> accountGroupJoin = accountRoot.join(
            "accountGroups", JoinType.INNER);
        Join<AccountGroup, GroupState> groupStateJoin = accountGroupJoin.join(
            "groupState", JoinType.INNER);

        query.select(cb.construct(
            GroupSummaryResponse.class,
            /* totalGroups */
            cb.countDistinct(accountGroupJoin.get("id"))
        ));

        List<Predicate> predicateList = getCommonPredicates(
            accountRoot, accountId, socialNetworkId);

        if (groupStateJoin != null) {
            predicateList.add(
                cb.equal(groupStateJoin.get("code"), GroupStateCodeEnum.JOINED.toString()));
        }

        query.where(cb.and(predicateList.toArray(Predicate[]::new)));

        return entityManager.createQuery(query)
            .getSingleResult();
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
