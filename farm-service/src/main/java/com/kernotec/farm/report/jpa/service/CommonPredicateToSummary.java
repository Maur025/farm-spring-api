package com.kernotec.farm.report.jpa.service;

import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.report.rest.dto.request.ActivitySummaryByAccountRequest;
import com.kernotec.farm.report.rest.dto.response.account.AccountSummaryTableResponse;
import com.kernotec.farm.util.CommonSpecification;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service class providing common predicate generation and pagination utilities for summary reports.
 * Facilitates building reusable JPA criteria queries for account and activity summaries.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CommonPredicateToSummary {

    private final EntityManager entityManager;
    private CriteriaBuilder cb;

    @PostConstruct
    private void init() {
        this.cb = entityManager.getCriteriaBuilder();
    }

    /**
     * Generates common predicates to filter accounts by account ID and social network ID. Useful
     * for building reusable account-related queries in summary reports.
     *
     * @param root            Root entity for Account in the query.
     * @param accountId       Unique identifier of the account.
     * @param socialNetworkId Unique identifier of the social network.
     * @return List of predicates for account filtering.
     */
    public List<Predicate> getCommonAccountPredicates(Root<Account> root, UUID accountId,
        UUID socialNetworkId)
    {
        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(cb.equal(root.get("id"), accountId));
        predicateList.add(cb.equal(root.get("socialNetworkId"), socialNetworkId));

        return predicateList;
    }

    /**
     * Adds a time-lapse predicate to the given predicate list, based on the filter request. Applies
     * date range filtering for activity summaries, considering the user's time zone.
     *
     * @param filterRequest          Request object containing date filter and time zone.
     * @param predicateList          List of predicates to be updated.
     * @param zonedDateTimeToCompare Path to the date field to compare.
     */
    public void addTimeLapsePredicate(ActivitySummaryByAccountRequest filterRequest,
        List<Predicate> predicateList, Path<ZonedDateTime> zonedDateTimeToCompare)
    {
        if (filterRequest.getFilterDate() == null || filterRequest.getZoneId() == null
            || filterRequest.getZoneId()
            .isBlank())
        {
            log.debug("No time lapse filter applied");

            return;
        }

        ZoneId userZone = ZoneId.of(filterRequest.getZoneId());

        predicateList.add(CommonSpecification.addTimeLapsePredicateByEnum(
            cb, userZone,
            filterRequest.getFilterDate(), zonedDateTimeToCompare
        ));
    }

    /**
     * Applies pagination to a JPA TypedQuery, returning a Spring Data Page object. Useful for
     * paginated responses in summary tables.
     *
     * @param query      JPA TypedQuery to paginate.
     * @param pageable   Pageable object containing pagination info.
     * @param totalItems Total number of items for the query.
     * @param <DTO>      Type of the DTO returned.
     * @return Paginated result as a Page object.
     */
    public <DTO> Page<DTO> applyPagination(TypedQuery<DTO> query, Pageable pageable,
        Long totalItems)
    {

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<DTO> resultPaginateList = query.getResultList();

        return new PageImpl<>(resultPaginateList, pageable, totalItems);
    }

    /**
     * Executes a paginated query for account summary tables and wraps the result in a custom
     * PageResponse object for API responses.
     *
     * @param query      CriteriaQuery for AccountSummaryTableResponse.
     * @param pageable   Pageable object containing pagination info.
     * @param totalItems Total number of items for the query.
     * @return PageResponse containing paginated data and metadata.
     */
    public PageResponse<AccountSummaryTableResponse> getResultTableWithPagination(
        CriteriaQuery<AccountSummaryTableResponse> query, Pageable pageable, Long totalItems)
    {
        Page<AccountSummaryTableResponse> resultPage = applyPagination(
            entityManager.createQuery(query), pageable, totalItems);

        return PageResponse.<AccountSummaryTableResponse>builder()
            .data(resultPage.getContent())
            .pagination(PaginationResponse.builder()
                .pages(resultPage.getTotalPages())
                .count(resultPage.getTotalElements())
                .build())
            .build();
    }
}


