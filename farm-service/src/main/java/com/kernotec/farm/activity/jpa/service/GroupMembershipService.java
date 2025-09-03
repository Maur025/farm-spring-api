package com.kernotec.farm.activity.jpa.service;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.core.jpa.service.BaseServiceImpl;
import com.kernotec.farm.activity.jpa.entity.GroupMembership;
import com.kernotec.farm.activity.jpa.repository.GroupMembershipRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
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
}
