package com.kernotec.farm.inventory.jpa.specification.device;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.entity.Person;
import com.kernotec.farm.inventory.jpa.entity.Device;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public class DeviceSpecification implements Specification<Device> {

    private Root<Device> root;
    private CriteriaBuilder cb;

    private Join<Device, Account> accountJoin;
    private Join<Account, Person> personJoin;

    private UUID farmId;
    private UUID socialNetworkId;
    private String keyword;

    public static DeviceSpecification builder()
    {
        return new DeviceSpecification();
    }

    private Join<Device, Account> getAccountJoin() {
        if (accountJoin == null) {
            accountJoin = root.join("accounts", JoinType.INNER);
        }

        return accountJoin;
    }

    private Join<Account, Person> getPersonJoin() {
        if (personJoin == null) {
            personJoin = getAccountJoin().join("person", JoinType.INNER);
        }

        return personJoin;
    }

    @Override
    public Predicate toPredicate(Root<Device> root, CriteriaQuery<?> query, CriteriaBuilder cb)
    {
        this.root = root;
        this.cb = cb;

        List<Predicate> predicateList = new ArrayList<>();

        getByFarmId().ifPresent(predicateList::add);
        getBySocialNetworkId().ifPresent(predicateList::add);
        getByKeyword().ifPresent(predicateList::add);

        query.distinct(true);
        return cb.and(predicateList.toArray(Predicate[]::new));
    }

    public DeviceSpecification withSocialNetworkId(UUID socialNetworkId) {
        this.socialNetworkId = socialNetworkId;
        return this;
    }

    private Optional<Predicate> getBySocialNetworkId() {
        return Optional.ofNullable(this.socialNetworkId)
            .map(socialNetworkId -> cb.equal(
                getAccountJoin().get("socialNetworkId"),
                socialNetworkId
            ));
    }

    public DeviceSpecification withFarmId(UUID farmId) {
        this.farmId = farmId;
        return this;
    }

    private Optional<Predicate> getByFarmId() {
        return Optional.ofNullable(this.farmId)
            .map(farmId -> cb.equal(root.get("farmId"), farmId));
    }

    public DeviceSpecification withKeyword(String keyword) {
        this.keyword = keyword != null && !keyword.isBlank() ? keyword : null;
        return this;
    }

    private Optional<Predicate> getByKeyword() {
        return Optional.ofNullable(this.keyword)
            .map(keyword -> {
                String pattern = keyword.toLowerCase() + "%";
                return cb.or(
                    cb.equal(root.get("deviceNumber"), keyword),
                    cb.like(cb.lower(getAccountJoin().get("username")), pattern),
                    cb.like(cb.lower(getPersonJoin().get("name")), pattern),
                    cb.like(cb.lower(getPersonJoin().get("lastName")), pattern)
                );
            });
    }
}
