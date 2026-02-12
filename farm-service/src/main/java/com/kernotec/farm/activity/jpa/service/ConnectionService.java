package com.kernotec.farm.activity.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.activity.exception.FriendException;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.entity.Connection;
import com.kernotec.farm.activity.jpa.enums.ConnectionActionEnum;
import com.kernotec.farm.activity.jpa.enums.ConnectionTypeEnum;
import com.kernotec.farm.activity.jpa.repository.ConnectionRepository;
import com.kernotec.farm.activity.rest.dto.request.connection.ConnectionFindAllFilterRequest;
import com.kernotec.farm.parametric.jpa.entity.RequestState;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ConnectionService extends BaseServiceImpl<Connection, UUID> {

    private final ConnectionRepository repository;

    @Override
    protected String resourceName() {
        return "Connection";
    }

    @Override
    protected BaseRepository<Connection, UUID> repository() {
        return repository;
    }

    public Page<Connection> findAllWithFilters(ConnectionFindAllFilterRequest filterRequest,
        Pageable pageable)
    {
        return repository.findAll(
            (Specification<Connection>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();

                Join<Connection, Activity> activityJoin = root.join("activity", JoinType.INNER);

                if (filterRequest.getSocialNetworkId() != null) {
                    Join<Activity, Account> accountJoin = activityJoin.join(
                        "account", JoinType.INNER);

                    predicateList.add(cb.equal(
                        accountJoin.get("socialNetworkId"),
                        filterRequest.getSocialNetworkId()
                    ));
                }

                if (filterRequest.getAccountId() != null) {
                    predicateList.add(
                        cb.equal(activityJoin.get("accountId"), filterRequest.getAccountId()));
                }

                if (filterRequest.getRequestStateCode() != null) {
                    Join<Connection, RequestState> requestStateJoin = root.join(
                        "requestState", JoinType.INNER);

                    predicateList.add(cb.equal(
                        requestStateJoin.get("code"), filterRequest.getRequestStateCode()
                            .toString()
                    ));
                }

                if (filterRequest.getAction() != null) {
                    predicateList.add(cb.equal(root.get("action"), filterRequest.getAction()));
                }

                if (filterRequest.getKeyword() != null && !filterRequest.getKeyword()
                    .isBlank())
                {
                    String pattern = "%" + filterRequest.getKeyword()
                        .toLowerCase() + "%";

                    Join<Connection, Account> potentialFriendJoin = root.join(
                        "potentialFriendAccount", JoinType.INNER);

                    predicateList.add(
                        cb.or(cb.like(cb.lower(potentialFriendJoin.get("username")), pattern)));
                }

                return cb.and(predicateList.toArray(Predicate[]::new));
            }, pageable
        );
    }

    public Optional<Connection> findByPotentialFriendAccountIdAndActionAndRequestStateIdAndType(
        UUID potentialFriendAccountId, ConnectionActionEnum action, UUID requestStateId,
        ConnectionTypeEnum type)
    {
        return repository.findByPotentialFriendAccountIdAndActionAndRequestStateIdAndType(
            potentialFriendAccountId, action, requestStateId, type);
    }

    public Connection findByPotentialFriendAccountIdAndActionAndRequestStateIdAndTypeThrow(
        UUID potentialFriendAccountId, ConnectionActionEnum action, UUID requestStateId,
        ConnectionTypeEnum type)
    {
        return findByPotentialFriendAccountIdAndActionAndRequestStateIdAndType(
            potentialFriendAccountId, action, requestStateId, type).orElseThrow(
            () -> new FriendException(
                "connection.not.found", "'" + potentialFriendAccountId + "'",
                HttpStatus.CONFLICT.value()
            ));
    }
}
