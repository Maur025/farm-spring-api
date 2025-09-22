package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.entity.Friend;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.parametric.jpa.entity.FriendState;
import com.kernotec.farm.parametric.jpa.enums.FriendStateCodeEnum;
import com.kernotec.farm.report.rest.dto.request.ActivitySummaryByAccountRequest;
import com.kernotec.farm.report.rest.dto.response.account.AccountSummaryTableResponse;
import com.kernotec.farm.report.rest.dto.response.account.FriendSummaryResponse;
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
public class FriendSummaryService {

    private final EntityManager entityManager;
    private final CommonPredicateToSummary commonPredicateToSummary;

    private CriteriaBuilder cb;

    @PostConstruct
    private void init() {
        this.cb = entityManager.getCriteriaBuilder();
    }

    public FriendSummaryResponse getFriendSummary(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest)
    {
        FriendSummaryResponse totalsOfFriendSummary = getTotalsOfFriendSummary(
            accountId, filterRequest);

        return totalsOfFriendSummary.withFriends(getFriendsOfResult(accountId, filterRequest));
    }

    private FriendSummaryResponse getTotalsOfFriendSummary(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest)
    {
        CriteriaQuery<FriendSummaryResponse> queryFriendSummary = cb.createQuery(
            FriendSummaryResponse.class);
        Root<Account> root = queryFriendSummary.from(Account.class);

        Join<Account, Friend> friendJoin = root.join("friends", JoinType.INNER);
        Join<Friend, Account> targetAccountJoin = friendJoin.join("friendAccount", JoinType.INNER);

        queryFriendSummary.select(cb.construct(
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

        queryFriendSummary.where(cb.and(
            getPredicatesOfFriends(root, accountId, filterRequest, friendJoin).toArray(
                Predicate[]::new)));

        return entityManager.createQuery(queryFriendSummary)
            .getSingleResult();
    }

    private List<AccountSummaryTableResponse> getFriendsOfResult(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest)
    {
        CriteriaQuery<AccountSummaryTableResponse> queryFriendsOfResult = cb.createQuery(
            AccountSummaryTableResponse.class);
        Root<Account> accountRoot = queryFriendsOfResult.from(Account.class);

        Join<Account, Friend> friendJoin = accountRoot.join("friends", JoinType.INNER);
        Join<Friend, Account> targetAccountJoin = friendJoin.join("friendAccount", JoinType.LEFT);

        queryFriendsOfResult.select(cb.construct(
            AccountSummaryTableResponse.class,
            /* id -> accountId */
            targetAccountJoin.get("id"),
            /* name -> username */
            targetAccountJoin.get("username"),
            /* detail -> accountType */
            cb.selectCase()
                .when(cb.equal(targetAccountJoin.get("type"), AccountTypeEnum.EXTERNAL), "Externo")
                .otherwise("Interno")
        ));

        queryFriendsOfResult.where(cb.and(
            getPredicatesOfFriends(accountRoot, accountId, filterRequest, friendJoin).toArray(
                Predicate[]::new)));

        return entityManager.createQuery(queryFriendsOfResult)
            .getResultList();
    }

    private List<Predicate> getPredicatesOfFriends(Root<Account> accountRoot, UUID accountId,
        ActivitySummaryByAccountRequest filterRequest, Join<Account, Friend> friendJoin)
    {
        Join<Friend, FriendState> friendStateJoin = friendJoin.join("friendState", JoinType.INNER);

        List<Predicate> predicateList = commonPredicateToSummary.getCommonAccountPredicates(
            accountRoot, accountId, filterRequest.getSocialNetworkId());

        commonPredicateToSummary.addTimeLapsePredicate(
            filterRequest, predicateList, friendJoin.get("acceptedAt"));

        if (friendStateJoin != null) {
            predicateList.add(
                cb.equal(friendStateJoin.get("code"), FriendStateCodeEnum.ACTIVE.toString()));
        }

        return predicateList;
    }
}
