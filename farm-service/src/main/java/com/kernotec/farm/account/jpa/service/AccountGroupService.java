package com.kernotec.farm.account.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.entity.AccountGroup;
import com.kernotec.farm.account.jpa.repository.AccountGroupRepository;
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
public class AccountGroupService extends BaseServiceImpl<AccountGroup, UUID> {

    private final AccountGroupRepository repository;

    @Override
    protected String resourceName() {
        return "Account Group";
    }

    @Override
    protected BaseRepository<AccountGroup, UUID> repository() {
        return repository;
    }

    public Page<AccountGroup> findAllWithFilters(UUID accountId, UUID socialNetworkId,
        Pageable pageable)
    {
        return repository.findAll(
            (Specification<AccountGroup>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();

                if (accountId != null) {
                    predicateList.add(cb.equal(root.get("accountId"), accountId));
                }

                if (socialNetworkId != null) {
                    Join<AccountGroup, Account> accountJoin = root.join("account", JoinType.INNER);

                    predicateList.add(
                        cb.equal(accountJoin.get("socialNetworkId"), socialNetworkId));
                }

                query.distinct(true);
                return cb.and(predicateList.toArray(jakarta.persistence.criteria.Predicate[]::new));
            }, pageable
        );
    }
}
