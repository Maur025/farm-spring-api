package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.report.rest.dto.request.ActivitySummaryByAccountRequest;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
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
}
