package com.kernotec.farm.activity.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.activity.jpa.repository.ActivityRepository;
import com.kernotec.farm.rest.dto.request.activity.ActivityFindAllFilterRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ActivityService extends BaseServiceImpl<Activity, UUID> {

    private final ActivityRepository repository;

    @Override
    protected String resourceName() {
        return "Activity";
    }

    @Override
    protected BaseRepository<Activity, UUID> repository() {
        return repository;
    }

    public Page<Activity> findAllWithFilters(ActivityFindAllFilterRequest filterRequest,
        Pageable pageable)
    {
        return repository.findAll(
            (Specification<Activity>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();

                Join<Activity, Account> accountJoin = root.join("account", JoinType.INNER);
                Join<Account, Device> accountDeviceJoin = accountJoin.join(
                    "devices", JoinType.INNER);

                if (filterRequest.getSocialNetworkId() != null) {
                    predicateList.add(cb.equal(
                        accountJoin.get("socialNetworkId"),
                        filterRequest.getSocialNetworkId()
                    ));
                }

                if (filterRequest.getDeviceId() != null) {
                    predicateList.add(
                        cb.equal(accountDeviceJoin.get("id"), filterRequest.getDeviceId()));
                }

                if (filterRequest.getAccountId() != null) {
                    predicateList.add(
                        cb.equal(root.get("accountId"), filterRequest.getAccountId()));
                }

                return cb.and(predicateList.toArray(Predicate[]::new));
            }, pageable
        );
    }
}
