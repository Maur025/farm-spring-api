package com.kernotec.farm.activity.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.activity.jpa.entity.Group;
import com.kernotec.farm.activity.jpa.entity.GroupMembership;
import com.kernotec.farm.parametric.jpa.entity.SocialNetwork;
import com.kernotec.farm.activity.jpa.repository.GroupRepository;
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
public class GroupService extends BaseServiceImpl<Group, UUID> {

    private final GroupRepository repository;

    @Override
    protected String resourceName() {
        return "Group";
    }

    @Override
    protected BaseRepository<Group, UUID> repository() {
        return repository;
    }

    public Page<Group> searchByName(String name, UUID socialNetworkId, Pageable pageable) {
        String pattern = name == null || name.isBlank() ? null : name.toLowerCase() + "%";

        return repository.findAll(
            (Specification<Group>) (root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();

                if (socialNetworkId != null) {
                    Join<Group, GroupMembership> groupMembershipJoin = root.join(
                        "groupMemberships",
                        JoinType.INNER
                    );
                    Join<GroupMembership, ActivityType> activityTypeJoin = groupMembershipJoin.join(
                        "activityType", JoinType.INNER);

                    Join<ActivityType, SocialNetwork> socialNetworkJoin = activityTypeJoin.join(
                        "socialNetworks", JoinType.INNER);

                    predicateList.add(cb.equal(socialNetworkJoin.get("id"), socialNetworkId));
                }

                if (pattern != null) {
                    predicateList.add(cb.like(cb.lower(root.get("name")), pattern));
                }

                return cb.and(predicateList.toArray(Predicate[]::new));
            }, pageable
        );
    }
}
