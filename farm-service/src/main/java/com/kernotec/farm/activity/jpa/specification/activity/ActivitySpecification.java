package com.kernotec.farm.activity.jpa.specification.activity;

import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.specification.criteria.ActivitySpecificationCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public record ActivitySpecification(ActivitySpecificationCriteria criteria) implements
    Specification<Activity>
{

    public static ActivitySpecification builder() {
        return new ActivitySpecification(new ActivitySpecificationCriteria());
    }

    private Join<?, ?> getOrCreateAccountJoin(
        Map<ActivitySpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(ActivitySpecificationJoinEnum.ACCOUNT_JOIN)) {
            joinMap.put(
                ActivitySpecificationJoinEnum.ACCOUNT_JOIN,
                root.join("account", JoinType.INNER)
            );
        }

        return joinMap.get(ActivitySpecificationJoinEnum.ACCOUNT_JOIN);
    }

    private Join<?, ?> getOrCreateDeviceJoin(Map<ActivitySpecificationJoinEnum, Join<?, ?>> joinMap,
        Root<?> root)
    {
        if (!joinMap.containsKey(ActivitySpecificationJoinEnum.DEVICE_JOIN)) {
            joinMap.put(
                ActivitySpecificationJoinEnum.DEVICE_JOIN,
                getOrCreateAccountJoin(joinMap, root).join("devices", JoinType.INNER)
            );
        }

        return joinMap.get(ActivitySpecificationJoinEnum.DEVICE_JOIN);
    }

    private Join<?, ?> getOrCreateFarmJoin(Map<ActivitySpecificationJoinEnum, Join<?, ?>> joinMap,
        Root<?> root)
    {
        if (!joinMap.containsKey(ActivitySpecificationJoinEnum.FARM_JOIN)) {
            joinMap.put(
                ActivitySpecificationJoinEnum.FARM_JOIN,
                getOrCreateDeviceJoin(joinMap, root).join("farm", JoinType.LEFT)
            );
        }

        return joinMap.get(ActivitySpecificationJoinEnum.FARM_JOIN);
    }

    @Override
    public Predicate toPredicate(Root<Activity> root, CriteriaQuery<?> query, CriteriaBuilder cb)
    {
        List<Predicate> predicateList = new ArrayList<>();
        Map<ActivitySpecificationJoinEnum, Join<?, ?>> joinMap = new HashMap<>();

        includeOnlyUserActivitiesFilter(root, cb).ifPresent(predicateList::add);
        addUserAuthIdFilter(root, cb).ifPresent(predicateList::add);
        addSocialNetworkIdFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addDeviceIdFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addFarmIdFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addActivityTypeIdFilter(root, cb).ifPresent(predicateList::add);
        addAccountIdFilter(root, cb).ifPresent(predicateList::add);
        addAccountIdListFilter(root).ifPresent(predicateList::add);
        addSimpleDateFilter(root, cb).ifPresent(predicateList::add);
        addDateRangeFilter(root, cb).ifPresent(predicateList::add);
        addMonthDateFilter(root, cb).ifPresent(predicateList::add);
        addYearDateFilter(root, cb).ifPresent(predicateList::add);
        addKeywordFilter(root, cb, joinMap).ifPresent(predicateList::add);

        query.distinct(true);
        return cb.and(predicateList.toArray(Predicate[]::new));
    }

    public ActivitySpecification includeOnlyUserActivities(Boolean includeOnlyUserActivities) {
        this.criteria.setIncludeOnlyUserActivities(includeOnlyUserActivities);
        return this;
    }

    private Optional<Predicate> includeOnlyUserActivitiesFilter(Root<Activity> root,
        CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getIncludeOnlyUserActivities())
            .map(includeOnlyUserActivities -> {
                if (includeOnlyUserActivities.equals(Boolean.FALSE)) {
                    return cb.conjunction();
                }

                return cb.isFalse(root.get("isSystemActivity"));
            });
    }

    public ActivitySpecification withUserAuthId(UUID userAuthId) {
        this.criteria.setUserAuthId(userAuthId);
        return this;
    }

    private Optional<Predicate> addUserAuthIdFilter(Root<Activity> root, CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getUserAuthId())
            .map(userAuthId -> cb.equal(root.get("createdBy"), userAuthId.toString()));
    }

    public ActivitySpecification withSocialNetworkId(UUID socialNetworkId) {
        this.criteria.setSocialNetworkId(socialNetworkId);
        return this;
    }

    private Optional<Predicate> addSocialNetworkIdFilter(Root<Activity> root, CriteriaBuilder cb,
        Map<ActivitySpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getSocialNetworkId())
            .map(socialNetworkId -> cb.equal(
                getOrCreateAccountJoin(joinMap, root).get("socialNetworkId"), socialNetworkId));
    }

    public ActivitySpecification withDeviceId(UUID deviceId) {
        this.criteria.setDeviceId(deviceId);
        return this;
    }

    private Optional<Predicate> addDeviceIdFilter(Root<Activity> root, CriteriaBuilder cb,
        Map<ActivitySpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getDeviceId())
            .map(deviceId -> cb.equal(getOrCreateDeviceJoin(joinMap, root).get("id"), deviceId));
    }

    public ActivitySpecification withFarmId(UUID farmId) {
        this.criteria.setFarmId(farmId);
        return this;
    }

    private Optional<Predicate> addFarmIdFilter(Root<Activity> root, CriteriaBuilder cb,
        Map<ActivitySpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getFarmId())
            .map(farmId -> cb.equal(getOrCreateDeviceJoin(joinMap, root).get("farmId"), farmId));
    }

    public ActivitySpecification withActivityTypeId(UUID activityTypeId) {
        this.criteria.setActivityTypeId(activityTypeId);
        return this;
    }

    private Optional<Predicate> addActivityTypeIdFilter(Root<Activity> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getActivityTypeId())
            .map(activityTypeId -> cb.equal(root.get("activityTypeId"), activityTypeId));
    }

    public ActivitySpecification withAccountId(UUID accountId) {
        this.criteria.setAccountId(accountId);
        return this;
    }

    private Optional<Predicate> addAccountIdFilter(Root<Activity> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getAccountId())
            .map(accountId -> cb.equal(root.get("accountId"), accountId));
    }

    public ActivitySpecification withAccountIdList(Set<UUID> accountIdList) {
        this.criteria.setAccountIdList(
            accountIdList != null && !accountIdList.isEmpty() ? accountIdList : null);

        return this;
    }

    private Optional<Predicate> addAccountIdListFilter(Root<Activity> root) {
        return Optional.ofNullable(criteria.getAccountIdList())
            .map(accountIdList -> root.get("accountId")
                .in(accountIdList));
    }

    public ActivitySpecification withSimpleDate(ZonedDateTime simpleDate) {
        this.criteria.setSimpleDate(simpleDate);
        return this;
    }

    private Optional<Predicate> addSimpleDateFilter(Root<Activity> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getSimpleDate())
            .map(simpleDate -> {
                log.info("zoneId: {}", criteria.getZoneId());
                log.info("simpleDate: {}", simpleDate);

                ZoneId userZoneId = getZoneId();

                LocalDate simpleLocalDate = simpleDate.toLocalDate();
                log.info("simpleLocalDate: {}", simpleLocalDate);

                ZonedDateTime startOfDay = simpleLocalDate.atStartOfDay(userZoneId);

                ZonedDateTime endOfDay = simpleLocalDate.atTime(LocalTime.MAX)
                    .atZone(userZoneId);

                log.info("startOfDay: {}", startOfDay);
                log.info("endOfDay: {}", endOfDay);

                return cb.between(root.get("activityDate"), startOfDay, endOfDay);
            });
    }

    public ActivitySpecification withDateRange(ZonedDateTime fromDate, ZonedDateTime toDate) {
        this.criteria.setFromDate(fromDate);
        this.criteria.setToDate(toDate);
        return this;
    }

    private Optional<Predicate> addDateRangeFilter(Root<Activity> root, CriteriaBuilder cb) {
        ZonedDateTime from = criteria.getFromDate();
        ZonedDateTime to = criteria.getToDate();

        if (from != null && to != null) {
            ZoneId userZoneId = getZoneId();
            LocalDate fromLocalDate = from.toLocalDate();
            ZonedDateTime startOfFromDate = fromLocalDate.atStartOfDay(userZoneId);

            LocalDate toLocalDate = to.toLocalDate();

            ZonedDateTime endOfToDate = toLocalDate.atTime(LocalTime.MAX)
                .atZone(userZoneId);

            log.info("from Date: {}", startOfFromDate);
            log.info("to Date: {}", endOfToDate);

            return Optional.of(cb.between(root.get("activityDate"), startOfFromDate, endOfToDate));
        }

        return Optional.empty();
    }

    public ActivitySpecification withMonthDate(ZonedDateTime monthDate) {
        this.criteria.setMonthDate(monthDate);
        return this;
    }

    private Optional<Predicate> addMonthDateFilter(Root<Activity> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getMonthDate())
            .map(monthDate -> {
                ZoneId userZoneId = getZoneId();

                LocalDate monthLocalDate = monthDate.withDayOfMonth(1)
                    .toLocalDate();

                ZonedDateTime startOfMonth = monthLocalDate.atStartOfDay(userZoneId);

                ZonedDateTime endOfMonth = startOfMonth.plusMonths(1)
                    .minusNanos(1);

                log.info("startOfMonth: {}", startOfMonth);
                log.info("endOfMonth: {}", endOfMonth);

                return cb.between(root.get("activityDate"), startOfMonth, endOfMonth);
            });
    }

    public ActivitySpecification withYearDate(ZonedDateTime yearDate) {
        this.criteria.setYearDate(yearDate);
        return this;
    }

    private Optional<Predicate> addYearDateFilter(Root<Activity> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getYearDate())
            .map(yearDate -> {
                ZoneId userZoneId = getZoneId();
                LocalDate yearLocalDate = yearDate.withDayOfYear(1)
                    .toLocalDate();

                ZonedDateTime startOfYear = yearLocalDate.atStartOfDay(userZoneId);

                ZonedDateTime endOfYear = startOfYear.plusYears(1)
                    .minusNanos(1);

                log.info("startOfYear: {}", startOfYear);
                log.info("endOfYear: {}", endOfYear);

                return cb.between(root.get("activityDate"), startOfYear, endOfYear);
            });
    }

    public ActivitySpecification withKeyword(String keyword) {
        this.criteria.setKeyword(keyword != null && !keyword.isBlank() ? keyword : null);
        return this;
    }

    private Optional<Predicate> addKeywordFilter(Root<Activity> root, CriteriaBuilder cb,
        Map<ActivitySpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getKeyword())
            .map(keyword -> {
                String pattern = "%" + criteria.getKeyword()
                    .toLowerCase() + "%";

                return cb.or(
                    cb.like(
                        cb.lower(getOrCreateAccountJoin(joinMap, root).get("username")), pattern),
                    cb.like(
                        cb.lower(getOrCreateDeviceJoin(joinMap, root).get("deviceNumber")),
                        pattern
                    ), cb.like(cb.lower(getOrCreateFarmJoin(joinMap, root).get("name")), pattern)
                );
            });
    }

    public ActivitySpecification withZoneId(String zoneId) {
        this.criteria.setZoneId(zoneId);
        return this;
    }

    private ZoneId getZoneId() {
        return ZoneId.of(this.criteria.getZoneId() != null ? this.criteria.getZoneId()
            : ZoneId.systemDefault()
                .toString());
    }
}
