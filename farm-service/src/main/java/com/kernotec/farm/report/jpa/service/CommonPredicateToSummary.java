package com.kernotec.farm.report.jpa.service;

import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.activity.jpa.entity.Activity;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        UUID socialNetworkId)
    {
        Join<Activity, Account> accountJoin = activityRoot.join("account");

        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(cb.equal(activityRoot.get("accountId"), accountId));
        predicateList.add(cb.equal(accountJoin.get("socialNetworkId"), socialNetworkId));

        return predicateList;
    }
}
