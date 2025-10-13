package com.kernotec.farm.account.jpa.specification.account;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.account.jpa.specification.criteria.AccountSpecificationCriteria;
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

public record AccountSpecification(AccountSpecificationCriteria criteria) implements
    Specification<Account>
{

    public static AccountSpecification builder() {
        return new AccountSpecification(new AccountSpecificationCriteria());
    }

    private Join<?, ?> getOrCreatePersonJoin(Map<AccountSpecificationJoinEnum, Join<?, ?>> joinMap,
        Root<?> root)
    {
        if (!joinMap.containsKey(AccountSpecificationJoinEnum.PERSON_JOIN)) {
            joinMap.put(
                AccountSpecificationJoinEnum.PERSON_JOIN, root.join("person", JoinType.INNER));
        }

        return joinMap.get(AccountSpecificationJoinEnum.PERSON_JOIN);
    }

    private Join<?, ?> getOrCreateDeviceJoin(Map<AccountSpecificationJoinEnum, Join<?, ?>> joinMap,
        Root<?> root)
    {
        if (!joinMap.containsKey(AccountSpecificationJoinEnum.DEVICE_JOIN)) {
            joinMap.put(
                AccountSpecificationJoinEnum.DEVICE_JOIN, root.join("devices", JoinType.INNER));
        }

        return joinMap.get(AccountSpecificationJoinEnum.DEVICE_JOIN);
    }

    private Join<?, ?> getOrCreateActivityJoin(
        Map<AccountSpecificationJoinEnum, Join<?, ?>> joinMap, Root<?> root)
    {
        if (!joinMap.containsKey(AccountSpecificationJoinEnum.ACTIVITY_JOIN)) {
            joinMap.put(
                AccountSpecificationJoinEnum.ACTIVITY_JOIN,
                root.join("activities", JoinType.LEFT)
            );
        }

        return joinMap.get(AccountSpecificationJoinEnum.ACTIVITY_JOIN);
    }

    @Override
    public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder cb)
    {
        List<Predicate> predicateList = new ArrayList<>();
        Map<AccountSpecificationJoinEnum, Join<?, ?>> joinMap = new HashMap<>();

        addSocialNetworkIdFilter(root, cb).ifPresent(predicateList::add);
        addIgnoreAccountIdFilter(root, cb).ifPresent(predicateList::add);
        addUsernameSearchFilter(root, cb).ifPresent(predicateList::add);
        addKeywordFilter(root, cb, joinMap).ifPresent(predicateList::add);
        addAccountTypeFilter(root, cb).ifPresent(predicateList::add);
        addLinkSearchFilter(root, cb).ifPresent(predicateList::add);

        query.distinct(true);
        return cb.and(predicateList.toArray(Predicate[]::new));
    }

    public AccountSpecification withSocialNetworkId(UUID socialNetworkId) {
        this.criteria.setSocialNetworkId(socialNetworkId);
        return this;
    }

    private Optional<Predicate> addSocialNetworkIdFilter(Root<Account> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getSocialNetworkId())
            .map(socialNetworkId -> cb.equal(root.get("socialNetworkId"), socialNetworkId));
    }

    public AccountSpecification ignoreAccountId(UUID accountId) {
        this.criteria.setIgnoreAccountId(accountId);
        return this;
    }

    private Optional<Predicate> addIgnoreAccountIdFilter(Root<Account> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getIgnoreAccountId())
            .map(accountId -> cb.notEqual(root.get("id"), accountId));
    }

    public AccountSpecification withUsernameSearch(String username) {
        this.criteria.setUsernameSearch(username != null && !username.isBlank() ? username : null);
        return this;
    }

    private Optional<Predicate> addUsernameSearchFilter(Root<Account> root, CriteriaBuilder cb) {
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

    private Optional<Predicate> addKeywordFilter(Root<Account> root, CriteriaBuilder cb,
        Map<AccountSpecificationJoinEnum, Join<?, ?>> joinMap)
    {
        return Optional.ofNullable(criteria.getKeyword())
            .map(keyword -> {
                String pattern = keyword.toLowerCase() + "%";

                return cb.or(
                    cb.like(cb.lower(root.get("username")), pattern),
                    cb.like(cb.lower(getOrCreatePersonJoin(joinMap, root).get("name")), pattern),
                    cb.like(
                        cb.lower(getOrCreatePersonJoin(joinMap, root).get("lastName")),
                        pattern
                    ), cb.equal(
                        cb.lower(getOrCreateDeviceJoin(joinMap, root).get("deviceNumber")),
                        keyword
                    )
                );
            });
    }

    public AccountSpecification withAccountType(AccountTypeEnum accountType) {
        this.criteria.setAccountType(accountType);
        return this;
    }

    private Optional<Predicate> addAccountTypeFilter(Root<Account> root, CriteriaBuilder cb) {
        return Optional.ofNullable(criteria.getAccountType())
            .map(accountType -> cb.equal(root.get("type"), accountType));
    }

    public AccountSpecification withLinkSearch(String link) {
        this.criteria.setLinkSearch(link != null && !link.isBlank() ? link : null);
        return this;
    }

    private Optional<Predicate> addLinkSearchFilter(Root<Account> root, CriteriaBuilder cb)
    {
        return Optional.ofNullable(criteria.getLinkSearch())
            .map(link -> cb.equal(root.get("accountLink"), link));
    }

    public AccountSpecification withZoneId(String zoneId) {
        this.criteria.setZoneId(zoneId);
        return this;
    }
}
