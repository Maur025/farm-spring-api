package com.kernotec.farm.activity.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.entity.GroupMembership;
import com.kernotec.farm.activity.jpa.repository.GroupMembershipRepository;
import com.kernotec.farm.activity.rest.dto.request.group.membership.GroupMembershipFindAllFilterRequest;
import com.kernotec.farm.parametric.jpa.entity.RequestState;
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
public class GroupMembershipService extends BaseServiceImpl<GroupMembership, UUID> {

    private final GroupMembershipRepository repository;

    @Override
    protected String resourceName() {
        return "Group Membership";
    }

    @Override
    protected BaseRepository<GroupMembership, UUID> repository() {
        return repository;
    }

    public Page<GroupMembership> findAllWithFilters(
        GroupMembershipFindAllFilterRequest filterRequest, Pageable pageable)
    {
        return repository.findAll(
            (Specification<GroupMembership>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();

                Join<GroupMembership, Activity> activityJoin = root.join(
                    "activity", JoinType.INNER);

                if (filterRequest.getAccountId() != null) {
                    predicateList.add(
                        cb.equal(activityJoin.get("accountId"), filterRequest.getAccountId()));
                }

                if (filterRequest.getSocialNetworkId() != null) {
                    Join<Activity, Account> accountJoin = activityJoin.join(
                        "account", JoinType.INNER);

                    predicateList.add(cb.equal(
                        accountJoin.get("socialNetworkId"),
                        filterRequest.getSocialNetworkId()
                    ));
                }

                if (filterRequest.getRequestStateCode() != null) {
                    Join<GroupMembership, RequestState> requestStateJoin = root.join(
                        "requestState", JoinType.INNER);

                    predicateList.add(cb.equal(
                        requestStateJoin.get("code"), filterRequest.getRequestStateCode()
                            .toString()
                    ));
                }

                if (filterRequest.getAction() != null) {
                    predicateList.add(cb.equal(root.get("action"), filterRequest.getAction()));
                }

                return cb.and(predicateList.toArray(Predicate[]::new));
            }, pageable
        );
    }
}
