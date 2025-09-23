package com.kernotec.farm.report.jpa.service;

import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.report.rest.dto.request.ActivitySummaryByAccountRequest;
import com.kernotec.farm.report.rest.dto.response.account.AccountSummaryTableResponse;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
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

    public List<Predicate> getCommonAccountPredicates(Root<Account> root, UUID accountId,
        UUID socialNetworkId)
    {
        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(cb.equal(root.get("id"), accountId));
        predicateList.add(cb.equal(root.get("socialNetworkId"), socialNetworkId));

        return predicateList;
    }

    public List<Predicate> getCommonActivityPredicates(Root<Activity> activityRoot, UUID accountId,
        ActivitySummaryByAccountRequest filterRequest)
    {
        Join<Activity, Account> accountJoin = activityRoot.join("account");

        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(cb.equal(activityRoot.get("accountId"), accountId));
        predicateList.add(
            cb.equal(accountJoin.get("socialNetworkId"), filterRequest.getSocialNetworkId()));

        addTimeLapsePredicate(filterRequest, predicateList, activityRoot.get("activityDate"));

        return predicateList;
    }

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

        ZonedDateTime to = ZonedDateTime.now(userZone);
        ZonedDateTime toStartOfDay = to.toLocalDate()
            .atStartOfDay(userZone);
        ZonedDateTime toEndOfDay = toStartOfDay.plusDays(1)
            .minusNanos(1);

        ZonedDateTime from = switch (filterRequest.getFilterDate()) {
            case ALL -> ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, userZone);
            case LAST_WEEK -> to.minusWeeks(1);
            case LAST_MONTH -> to.minusMonths(1);
            case LAST_THREE_MONTHS -> to.minusMonths(3);
        };

        ZonedDateTime fromStartOfDay = from.toLocalDate()
            .atStartOfDay(userZone);

        log.info("Applying time lapse filter from {} to {}", fromStartOfDay, toEndOfDay);

        log.info("Applying time lapse filter to {}", zonedDateTimeToCompare);

        predicateList.add(cb.between(zonedDateTimeToCompare, fromStartOfDay, toEndOfDay));
    }

    public <DTO> Page<DTO> applyPagination(TypedQuery<DTO> query, Pageable pageable,
        Long totalItems)
    {

        log.info("Paginated size: {}", pageable.getPageSize());

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<DTO> resultPaginateList = query.getResultList();

        return new PageImpl<>(resultPaginateList, pageable, totalItems);
    }

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
