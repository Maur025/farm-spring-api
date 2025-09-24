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

    @Override
    public Predicate toPredicate(Root<Activity> root, CriteriaQuery<?> query, CriteriaBuilder cb)
    {
        List<Predicate> predicateList = new ArrayList<>();
        Map<ActivitySpecificationJoinEnum, Join<?, ?>> joinMap = new HashMap<>();

        includeOnlyUserActivitiesFilter(root, cb).ifPresent(predicateList::add);
        addUserAuthIdFilter(root, cb).ifPresent(predicateList::add);
        addSocialNetworkIdFilter(root, cb, joinMap).ifPresent(predicateList::add);

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
}
