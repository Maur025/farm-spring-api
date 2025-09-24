package com.kernotec.farm.account.jpa.specification.account;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.entity.Person;
import com.kernotec.farm.account.jpa.specification.criteria.AccountSpecificationCriteria;
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
import org.springframework.data.jpa.domain.Specification;

public class AccountSpecification implements Specification<Account> {

    private final AccountSpecificationCriteria criteria = new AccountSpecificationCriteria();

    private Root<Account> root;
    private CriteriaBuilder cb;

    private Join<Account, Person> personJoin;
    private Join<Account, Device> deviceJoin;

    public static AccountSpecification builder() {
        return new AccountSpecification();
    }

    private Join<Account, Person> getPersonJoin() {
        if (personJoin == null) {
            personJoin = root.join("person", JoinType.INNER);
        }

        return personJoin;
    }

    private Join<Account, Device> getDeviceJoin() {
        if (deviceJoin == null) {
            deviceJoin = root.join("devices", JoinType.INNER);
        }

        return deviceJoin;
    }

    @Override
    public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder cb)
    {
        this.root = root;
        this.cb = cb;

        List<Predicate> predicateList = new ArrayList<>();

        addSocialNetworkIdFilter().ifPresent(predicateList::add);
        addIgnoreAccountIdFilter().ifPresent(predicateList::add);
        addUsernameSearchFilter().ifPresent(predicateList::add);
        addKeywordFilter().ifPresent(predicateList::add);

        query.distinct(true);
        return cb.and(predicateList.toArray(Predicate[]::new));
    }

    public AccountSpecification withSocialNetworkId(UUID socialNetworkId) {
        this.criteria.setSocialNetworkId(socialNetworkId);
        return this;
    }

    private Optional<Predicate> addSocialNetworkIdFilter() {
        return Optional.ofNullable(criteria.getSocialNetworkId())
            .map(socialNetworkId -> cb.equal(root.get("socialNetworkId"), socialNetworkId));
    }

    public AccountSpecification ignoreAccountId(UUID accountId) {
        this.criteria.setIgnoreAccountId(accountId);
        return this;
    }

    private Optional<Predicate> addIgnoreAccountIdFilter() {
        return Optional.ofNullable(criteria.getIgnoreAccountId())
            .map(accountId -> cb.notEqual(root.get("id"), accountId));
    }

    public AccountSpecification withUsernameSearch(String username) {
        this.criteria.setUsernameSearch(username != null && !username.isBlank() ? username : null);
        return this;
    }

    private Optional<Predicate> addUsernameSearchFilter() {
        return Optional.ofNullable(criteria.getUsernameSearch())
            .map(username -> {
                String pattern = "%" + username.toLowerCase() + "%";
                return cb.like(cb.lower(root.get("username")), pattern);
            });
    }

    public AccountSpecification withKeyword(String keyword) {
        this.criteria.setKeyword(keyword != null && !keyword.isBlank() ? keyword : null);
        return this;
    }

    private Optional<Predicate> addKeywordFilter() {
        return Optional.ofNullable(criteria.getKeyword())
            .map(keyword -> {
                String pattern = keyword.toLowerCase() + "%";

                return cb.or(
                    cb.like(cb.lower(root.get("username")), pattern),
                    cb.like(cb.lower(getPersonJoin().get("name")), pattern),
                    cb.like(cb.lower(getPersonJoin().get("lastName")), pattern),
                    cb.equal(cb.lower(getDeviceJoin().get("deviceNumber")), keyword)
                );
            });
    }
}
