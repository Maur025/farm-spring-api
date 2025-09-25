package com.kernotec.farm.inventory.jpa.specification.device;

import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.jpa.specification.criteria.DeviceSpecificationCriteria;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public record DeviceSpecification(DeviceSpecificationCriteria criteria) implements
    Specification<Device>
{

    public static DeviceSpecification builder()
    {
        return new DeviceSpecification(new DeviceSpecificationCriteria());
    }

    private Join<?, ?> getOrCreateAccountJoin(Map<DeviceSpecificationJoinEnum, Join<?, ?>> joinMap,
        Root<?> root)
    {
        if (!joinMap.containsKey(DeviceSpecificationJoinEnum.ACCOUNT_JOIN)) {
            joinMap.put(
                DeviceSpecificationJoinEnum.ACCOUNT_JOIN, root.join("accounts", JoinType.INNER));
        }

        return joinMap.get(DeviceSpecificationJoinEnum.ACCOUNT_JOIN);
    }

    private Join<?, ?> getOrCreatePersonJoin(Map<DeviceSpecificationJoinEnum, Join<?, ?>> joinMap,
        Root<?> root)
    {
        if (!joinMap.containsKey(DeviceSpecificationJoinEnum.PERSON_JOIN)) {
            joinMap.put(
                DeviceSpecificationJoinEnum.PERSON_JOIN,
                getOrCreateAccountJoin(joinMap, root).join("person", JoinType.INNER)
            );
        }

        return joinMap.get(DeviceSpecificationJoinEnum.PERSON_JOIN);
    }

    @Override
    public Predicate toPredicate(Root<Device> root, CriteriaQuery<?> query, CriteriaBuilder cb)
    {
        List<Predicate> predicateList = new ArrayList<>();
        Map<DeviceSpecificationJoinEnum, Join<?, ?>> joinMap = new HashMap<>();

        getByFarmId(root, cb).ifPresent(predicateList::add);
        getBySocialNetworkId(root, cb, joinMap).ifPresent(predicateList::add);
        getByKeyword(root, cb, joinMap).ifPresent(predicateList::add);

        if (criteria.getOrderBy() != null) {
            query.orderBy(criteria.isDescending() ? cb.desc(root.get(criteria.getOrderBy()))
                : cb.asc(root.get(criteria.getOrderBy())));
        }

        query.distinct(true);
        return cb.and(predicateList.toArray(Predicate[]::new));
    }

    public DeviceSpecification withOrderBy(String orderBy, boolean isDescending) {
        this.criteria.setOrderBy(orderBy);
        this.criteria.setDescending(isDescending);
        return this;
    }

    public DeviceSpecification withSocialNetworkId(UUID socialNetworkId) {
        this.criteria.setSocialNetworkId(socialNetworkId);
        return this;
    }

    private Optional<Predicate> getBySocialNetworkId(Root<Device> root, CriteriaBuilder cb,
        Map<DeviceSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getSocialNetworkId())
            .map(socialNetworkId -> cb.equal(
                getOrCreateAccountJoin(joinMap, root).get("socialNetworkId"), socialNetworkId));
    }

    public DeviceSpecification withFarmId(UUID farmId) {
        this.criteria.setFarmId(farmId);
        return this;
    }

    private Optional<Predicate> getByFarmId(Root<Device> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getFarmId())
            .map(farmId -> cb.equal(root.get("farmId"), farmId));
    }

    public DeviceSpecification withKeyword(String keyword) {
        this.criteria.setKeyword(keyword != null && !keyword.isBlank() ? keyword : null);
        return this;
    }

    private Optional<Predicate> getByKeyword(Root<Device> root, CriteriaBuilder cb,
        Map<DeviceSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getKeyword())
            .map(keyword -> {
                String pattern = keyword.toLowerCase() + "%";
                return cb.or(
                    cb.equal(root.get("deviceNumber"), keyword),
                    cb.like(
                        cb.lower(getOrCreateAccountJoin(joinMap, root).get("username")),
                        pattern
                    ), cb.like(cb.lower(getOrCreatePersonJoin(joinMap, root).get("name")), pattern),
                    cb.like(cb.lower(getOrCreatePersonJoin(joinMap, root).get("lastName")), pattern)
                );
            });
    }
}
