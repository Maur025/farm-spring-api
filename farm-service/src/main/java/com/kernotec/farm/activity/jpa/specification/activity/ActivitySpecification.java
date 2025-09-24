package com.kernotec.farm.activity.jpa.specification.activity;

import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.specification.criteria.ActivitySpecificationCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

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
}
