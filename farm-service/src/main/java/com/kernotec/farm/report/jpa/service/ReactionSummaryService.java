package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.entity.Reaction;
import com.kernotec.farm.parametric.jpa.entity.ReactionType;
import com.kernotec.farm.report.rest.dto.request.ActivitySummaryByAccountRequest;
import com.kernotec.farm.report.rest.dto.response.account.ReactionSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.ReactionTypeSummaryResponse;
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
public class ReactionSummaryService {

    private final EntityManager entityManager;
    private final CommonPredicateToSummary commonPredicateToSummary;

    private CriteriaBuilder cb;

    @PostConstruct
    private void init() {
        this.cb = entityManager.getCriteriaBuilder();
    }

    public ReactionSummaryResponse getReactionSummary(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest)
    {
        return ReactionSummaryResponse.builder()
            .totalReactions(getTotalReactions(accountId, filterRequest.getSocialNetworkId()))
            .totalsByType(getTotalReactionsByType(accountId, filterRequest.getSocialNetworkId()))
            .build();
    }

    private Long getTotalReactions(UUID accountId, UUID socialNetworkId) {
        CriteriaQuery<Long> queryOfTotalReactions = cb.createQuery(Long.class);
        Root<Activity> activityRoot = queryOfTotalReactions.from(Activity.class);

        Join<Activity, Reaction> reactionJoin = activityRoot.join("reactions", JoinType.INNER);

        queryOfTotalReactions.select(cb.countDistinct(reactionJoin.get("id")));

        queryOfTotalReactions.where(
            commonPredicateToSummary.getCommonActivityPredicates(
                    activityRoot, accountId,
                    socialNetworkId
                )
                .toArray(Predicate[]::new));

        return entityManager.createQuery(queryOfTotalReactions)
            .getSingleResult();
    }

    private List<ReactionTypeSummaryResponse> getTotalReactionsByType(UUID accountId,
        UUID socialNetworkId)
    {
        CriteriaQuery<ReactionTypeSummaryResponse> queryReactionsByType = cb.createQuery(
            ReactionTypeSummaryResponse.class);
        Root<Activity> activityRoot = queryReactionsByType.from(Activity.class);

        Join<Activity, Reaction> reactionJoin = activityRoot.join("reactions", JoinType.INNER);
        Join<Reaction, ReactionType> reactionTypeJoin = reactionJoin.join(
            "reactionType", JoinType.INNER);

        queryReactionsByType.select(cb.construct(
            ReactionTypeSummaryResponse.class,
            /* reactionTypeId */
            reactionTypeJoin.get("id"),
            /* reactionTypeName */
            reactionTypeJoin.get("name"),
            /* reactionTypeCode */
            reactionTypeJoin.get("code"),
            /* totalReactionsByType */
            cb.countDistinct(reactionJoin.get("id"))
        ));

        queryReactionsByType.where(cb.and(
            commonPredicateToSummary.getCommonActivityPredicates(
                    activityRoot, accountId,
                    socialNetworkId
                )
                .toArray(Predicate[]::new)));

        queryReactionsByType.groupBy(
            reactionTypeJoin.get("id"), reactionTypeJoin.get("name"), reactionTypeJoin.get("code"));

        return entityManager.createQuery(queryReactionsByType)
            .getResultList();
    }
}
