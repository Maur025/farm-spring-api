package com.kernotec.farm.report.jpa.service;

import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.entity.Comment;
import com.kernotec.farm.activity.jpa.specification.activity.ActivitySpecification;
import com.kernotec.farm.parametric.jpa.entity.PublishingContext;
import com.kernotec.farm.report.rest.dto.request.ActivitySummaryByAccountRequest;
import com.kernotec.farm.report.rest.dto.request.ReportDashboardRequest;
import com.kernotec.farm.report.rest.dto.response.account.AccountSummaryTableResponse;
import com.kernotec.farm.report.rest.dto.response.account.CommentSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.comment.CommentByContextResponse;
import com.kernotec.farm.util.CommonSpecification;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.List;
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
            CommonSpecification.getTotalBySumCase(cb, cb.isTrue(commentJoin.get("isAgreeComment"))),
            /* totalNegativeComments */
            CommonSpecification.getTotalBySumCase(cb, cb.isFalse(commentJoin.get("isAgreeComment")))
        ));

        queryCommentSummary.where(ActivitySpecification.builder()
            .includeOnlyUserActivities(true)
            .withZoneId(filterRequest.getZoneId())
            .withAccountId(accountId)
            .withSocialNetworkId(filterRequest.getSocialNetworkId())
            .withTemporaryDate(filterRequest.getFilterDate())
            .toPredicate(activityRoot, queryCommentSummary, cb));

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

        queryCommentsOfResult.where(ActivitySpecification.builder()
            .includeOnlyUserActivities(true)
            .withZoneId(filterRequest.getZoneId())
            .withAccountId(accountId)
            .withSocialNetworkId(filterRequest.getSocialNetworkId())
            .withTemporaryDate(filterRequest.getFilterDate())
            .toPredicate(activityRoot, queryCommentsOfResult, cb));

        if (filterRequest.getPageable() == null) {
            return PageResponse.<AccountSummaryTableResponse>builder()
                .data(entityManager.createQuery(queryCommentsOfResult)
                    .getResultList())
                .build();
        }

        return commonPredicateToSummary.getResultTableWithPagination(
            queryCommentsOfResult, filterRequest.getPageable(), totalComments);
    }

    public List<CommentByContextResponse> getTotalCommentsByContext(
        ReportDashboardRequest filterRequest)
    {
        CriteriaQuery<CommentByContextResponse> commentByContextQuery = cb.createQuery(
            CommentByContextResponse.class);

        Root<Activity> activityRoot = commentByContextQuery.from(Activity.class);
        Join<Activity, Comment> commentJoin = activityRoot.join("comments", JoinType.INNER);
        Join<Comment, PublishingContext> publishingContextJoin = commentJoin.join(
            "publishingContext", JoinType.LEFT);

        commentByContextQuery.select(cb.construct(
            CommentByContextResponse.class,
            // publishingContextId
            publishingContextJoin.get("id"),
            // publishingContextName
            publishingContextJoin.get("name"),
            // totalCommentsByContext
            cb.countDistinct(commentJoin.get("id"))
        ));

        commentByContextQuery.groupBy(
            publishingContextJoin.get("id"), publishingContextJoin.get("name"));

        commentByContextQuery.where(ActivitySpecification.builder()
            .includeOnlyUserActivities(true)
            .withSocialNetworkId(filterRequest.getSocialNetworkId())
            .withUserAuthId(filterRequest.getAuthUserId())
            .withMonthDate(filterRequest.getMonthDate())
            .withZoneId(filterRequest.getZoneId())
            .toPredicate(activityRoot, commentByContextQuery, cb));

        return entityManager.createQuery(commentByContextQuery)
            .getResultList();
    }
}
