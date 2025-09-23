package com.kernotec.farm.report.jpa.service;

import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.entity.Comment;
import com.kernotec.farm.parametric.jpa.entity.PublishingContext;
import com.kernotec.farm.report.rest.dto.request.ActivitySummaryByAccountRequest;
import com.kernotec.farm.report.rest.dto.response.account.AccountSummaryTableResponse;
import com.kernotec.farm.report.rest.dto.response.account.CommentSummaryResponse;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentSummaryService {

    private final EntityManager entityManager;
    private final CommonPredicateToSummary commonPredicateToSummary;

    private CriteriaBuilder cb;

    @PostConstruct
    private void init() {
        this.cb = entityManager.getCriteriaBuilder();
    }

    public CommentSummaryResponse getCommentSummary(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest)
    {
        CommentSummaryResponse commentSummaryResponse = getTotalsOfCommentSummary(
            accountId, filterRequest);

        return commentSummaryResponse.withComments(getCommentsOfResult(
            accountId, filterRequest,
            commentSummaryResponse.getTotalComments()
        ));
    }

    private CommentSummaryResponse getTotalsOfCommentSummary(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest)
    {
        CriteriaQuery<CommentSummaryResponse> queryCommentSummary = cb.createQuery(
            CommentSummaryResponse.class);
        Root<Activity> activityRoot = queryCommentSummary.from(Activity.class);

        Join<Activity, Comment> commentJoin = activityRoot.join("comments", JoinType.INNER);

        queryCommentSummary.select(cb.construct(
            CommentSummaryResponse.class,
            /* totalComments */
            cb.countDistinct(commentJoin.get("id")),
            /* totalPositiveComments */
            cb.coalesce(
                cb.sum(cb.<Long>selectCase()
                    .when(cb.isTrue(commentJoin.get("isAgreeComment")), 1L)
                    .otherwise(0L)), 0L
            ),
            /* totalNegativeComments */
            cb.coalesce(
                cb.sum(cb.<Long>selectCase()
                    .when(cb.isFalse(commentJoin.get("isAgreeComment")), 1L)
                    .otherwise(0L)), 0L
            )
        ));

        queryCommentSummary.where(cb.and(
            commonPredicateToSummary.getCommonActivityPredicates(
                    activityRoot, accountId,
                    filterRequest
                )
                .toArray(Predicate[]::new)));

        return entityManager.createQuery(queryCommentSummary)
            .getSingleResult();
    }

    private PageResponse<AccountSummaryTableResponse> getCommentsOfResult(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest, Long totalComments)
    {
        CriteriaQuery<AccountSummaryTableResponse> queryCommentsOfResult = cb.createQuery(
            AccountSummaryTableResponse.class);
        Root<Activity> activityRoot = queryCommentsOfResult.from(Activity.class);

        Join<Activity, Comment> commentJoin = activityRoot.join("comments", JoinType.INNER);
        Join<Comment, PublishingContext> publishingContextJoin = commentJoin.join(
            "publishingContext", JoinType.LEFT);

        queryCommentsOfResult.select(cb.construct(
            AccountSummaryTableResponse.class,
            /* id -> commentId */
            commentJoin.get("id"),
            /* name -> publishingContextName */
            publishingContextJoin.get("name"),
            /* detail -> isAgreeComment */
            cb.selectCase()
                .when(cb.isTrue(commentJoin.get("isAgreeComment")), "Positivo")
                .otherwise("Negativo")
        ));

        queryCommentsOfResult.where(cb.and(
            commonPredicateToSummary.getCommonActivityPredicates(
                    activityRoot, accountId,
                    filterRequest
                )
                .toArray(Predicate[]::new)));

        if (filterRequest.getPageable() == null) {
            return PageResponse.<AccountSummaryTableResponse>builder()
                .data(entityManager.createQuery(queryCommentsOfResult)
                    .getResultList())
                .build();
        }

        return commonPredicateToSummary.getResultTableWithPagination(
            queryCommentsOfResult, filterRequest.getPageable(), totalComments);
    }
}
