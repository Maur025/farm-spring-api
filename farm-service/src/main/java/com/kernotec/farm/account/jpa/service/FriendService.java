package com.kernotec.farm.account.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.entity.Friend;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.account.jpa.repository.FriendRepository;
import com.kernotec.farm.activity.exception.FriendException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FriendService extends BaseServiceImpl<Friend, UUID> {

    private final FriendRepository repository;

    @Override
    protected String resourceName() {
        return "Friend";
    }

    @Override
    protected BaseRepository<Friend, UUID> repository() {
        return repository;
    }

    public Page<Friend> findAllWithFilters(UUID accountId, UUID socialNetworkId,
        AccountTypeEnum friendAccountType, Pageable pageable)
    {
        return repository.findAll(
            (Specification<Friend>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();

                if (accountId != null) {
                    predicateList.add(cb.equal(root.get("accountId"), accountId));
                }

                if (socialNetworkId != null) {
                    Join<Friend, Account> accountJoin = root.join("account", JoinType.INNER);

                    predicateList.add(
                        cb.equal(accountJoin.get("socialNetworkId"), socialNetworkId));
                }

                if (friendAccountType != null) {
                    Join<Friend, Account> friendAccountJoin = root.join(
                        "friendAccount", JoinType.INNER);

                    predicateList.add(cb.equal(friendAccountJoin.get("type"), friendAccountType));
                }

                query.distinct(true);
                return cb.and(predicateList.toArray(Predicate[]::new));
            }, pageable
        );
    }

    public Optional<Friend> findByAccountIdAndFriendAccountIdAndFriendStateId(UUID accountId,
        UUID friendAccountId, UUID friendStateId)
    {
        return repository.findByAccountIdAndFriendAccountIdAndFriendStateId(
            accountId, friendAccountId, friendStateId);
    }

    public Friend findByAccountIdAndFriendAccountIdAndFriendStateIdThrow(UUID accountId,
        UUID friendAccountId, UUID friendStateId)
    {
        return findByAccountIdAndFriendAccountIdAndFriendStateId(
            accountId, friendAccountId, friendStateId).orElseThrow(() -> new FriendException(""));
    }
}
