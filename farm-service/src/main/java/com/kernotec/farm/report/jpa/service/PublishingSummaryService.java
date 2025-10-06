package com.kernotec.farm.report.jpa.service;

import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.entity.Publishing;
import com.kernotec.farm.activity.jpa.specification.activity.ActivitySpecification;
import com.kernotec.farm.parametric.jpa.entity.PublishingContext;
import com.kernotec.farm.parametric.jpa.entity.PublishingType;
import com.kernotec.farm.report.rest.dto.request.ActivitySummaryByAccountRequest;
import com.kernotec.farm.report.rest.dto.response.account.AccountSummaryTableResponse;
import com.kernotec.farm.report.rest.dto.response.account.PublishingContextSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.PublishingSummaryResponse;
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
public class PublishingSummaryService {

    private final EntityManager entityManager;
    private final CommonPredicateToSummary commonPredicateToSummary;
    private final ReportActivityService reportActivityService;

    private CriteriaBuilder cb;

    @PostConstruct
    private void init() {
        this.cb = entityManager.getCriteriaBuilder();
    }

    public PublishingSummaryResponse getPublishingSummary(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest)
    {
        Long totalPublications = reportActivityService.getTotalActivitiesByTypeToSummary(
            accountId, filterRequest, "publishings");

        return PublishingSummaryResponse.builder()
            .totalPublications(totalPublications)
            .totalsByContext(getTotalPublicationsByContext(accountId, filterRequest))
            .publications(getPublicationsOfResult(accountId, filterRequest, totalPublications))
            .build();
    }

    private PageResponse<AccountSummaryTableResponse> getPublicationsOfResult(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest, Long totalPublications)
    {
        CriteriaQuery<AccountSummaryTableResponse> queryPublicationsOfResult = cb.createQuery(
            AccountSummaryTableResponse.class);
        Root<Activity> activityRoot = queryPublicationsOfResult.from(Activity.class);

        Join<Activity, Publishing> publishingJoin = activityRoot.join(
            "publishings", JoinType.INNER);
        Join<Publishing, PublishingType> publishingTypeJoin = publishingJoin.join(
            "publishingType", JoinType.LEFT);
        Join<Publishing, PublishingContext> publishingContextJoin = publishingJoin.join(
            "publishingContext", JoinType.LEFT);

        queryPublicationsOfResult.select(cb.construct(
            AccountSummaryTableResponse.class,
            /* id -> publishingId */
            publishingJoin.get("id"),
            /* name -> publishinTypeName */
            publishingTypeJoin.get("name"),
            /* detail -> publishinContextName */
            publishingContextJoin.get("name")
        ));

        queryPublicationsOfResult.where(ActivitySpecification.builder()
            .includeOnlyUserActivities(true)
            .withZoneId(filterRequest.getZoneId())
            .withAccountId(accountId)
            .withSocialNetworkId(filterRequest.getSocialNetworkId())
            .withTemporaryDate(filterRequest.getFilterDate())
            .withMonthDate(filterRequest.getMonthDate())
            .withUserAuthId(filterRequest.getAuthUserId())
            .toPredicate(activityRoot, queryPublicationsOfResult, cb));

        if (filterRequest.getPageable() == null) {
            return PageResponse.<AccountSummaryTableResponse>builder()
                .data(entityManager.createQuery(queryPublicationsOfResult)
                    .getResultList())
                .build();
        }

        return commonPredicateToSummary.getResultTableWithPagination(
            queryPublicationsOfResult, filterRequest.getPageable(), totalPublications);
    }

    public List<PublishingContextSummaryResponse> getTotalPublicationsByContext(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest)
    {
        CriteriaQuery<PublishingContextSummaryResponse> queryPublicationsByContext = cb.createQuery(
            PublishingContextSummaryResponse.class);
        Root<Activity> activityRoot = queryPublicationsByContext.from(Activity.class);

        Join<Activity, Publishing> publishingJoin = activityRoot.join(
            "publishings", JoinType.INNER);
        Join<Publishing, PublishingContext> publishingContextJoin = publishingJoin.join(
            "publishingContext", JoinType.INNER);

        queryPublicationsByContext.select(cb.construct(
            PublishingContextSummaryResponse.class,
            /* publishingContextId */
            publishingContextJoin.get("id"),
            /* publishingContextName */
            publishingContextJoin.get("name"),
            /* totalPublicationsByContext */
            cb.countDistinct(publishingJoin.get("id"))
        ));

        queryPublicationsByContext.where(ActivitySpecification.builder()
            .includeOnlyUserActivities(true)
            .withZoneId(filterRequest.getZoneId())
            .withAccountId(accountId)
            .withSocialNetworkId(filterRequest.getSocialNetworkId())
            .withTemporaryDate(filterRequest.getFilterDate())
            .withMonthDate(filterRequest.getMonthDate())
            .withUserAuthId(filterRequest.getAuthUserId())
            .toPredicate(activityRoot, queryPublicationsByContext, cb));

        queryPublicationsByContext.groupBy(
            publishingContextJoin.get("id"), publishingContextJoin.get("name"));

        return entityManager.createQuery(queryPublicationsByContext)
            .getResultList();
    }
}
