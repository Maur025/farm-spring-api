package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.entity.Publishing;
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
import jakarta.persistence.criteria.Predicate;
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

    private CriteriaBuilder cb;

    @PostConstruct
    private void init() {
        this.cb = entityManager.getCriteriaBuilder();
    }

    public PublishingSummaryResponse getPublishingSummary(UUID accountId,
        ActivitySummaryByAccountRequest filterRequest)
    {
        return PublishingSummaryResponse.builder()
            .totalPublications(getTotalPublications(accountId, filterRequest.getSocialNetworkId()))
            .totalsByContext(
                getTotalPublicationsByContext(accountId, filterRequest.getSocialNetworkId()))
            .publications(getPublicationsOfResult(accountId, filterRequest.getSocialNetworkId()))
            .build();
    }

    private Long getTotalPublications(UUID accountId, UUID socialNetworkId) {
        CriteriaQuery<Long> queryOfTotalPublications = cb.createQuery(Long.class);
        Root<Activity> activityRoot = queryOfTotalPublications.from(Activity.class);

        Join<Activity, Publishing> publishingJoin = activityRoot.join(
            "publishings", JoinType.INNER);

        queryOfTotalPublications.select(cb.countDistinct(publishingJoin.get("id")));

        queryOfTotalPublications.where(
            commonPredicateToSummary.getCommonActivityPredicates(
                    activityRoot, accountId,
                    socialNetworkId
                )
                .toArray(Predicate[]::new));

        return entityManager.createQuery(queryOfTotalPublications)
            .getSingleResult();
    }

    private List<AccountSummaryTableResponse> getPublicationsOfResult(UUID accountId,
        UUID socialNetworkId)
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

        queryPublicationsOfResult.where(cb.and(
            commonPredicateToSummary.getCommonActivityPredicates(
                    activityRoot, accountId,
                    socialNetworkId
                )
                .toArray(Predicate[]::new)));

        return entityManager.createQuery(queryPublicationsOfResult)
            .getResultList();
    }

    private List<PublishingContextSummaryResponse> getTotalPublicationsByContext(UUID accountId,
        UUID socialNetworkId)
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

        queryPublicationsByContext.where(cb.and(
            commonPredicateToSummary.getCommonActivityPredicates(
                    activityRoot, accountId,
                    socialNetworkId
                )
                .toArray(Predicate[]::new)));

        queryPublicationsByContext.groupBy(
            publishingContextJoin.get("id"), publishingContextJoin.get("name"));

        return entityManager.createQuery(queryPublicationsByContext)
            .getResultList();
    }
}
