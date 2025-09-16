package com.kernotec.farm.activity.jpa.repository;

import com.kernotec.core.jpa.repository.BaseRepository;
import com.kernotec.farm.activity.jpa.entity.GroupMembership;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMembershipRepository extends BaseRepository<GroupMembership, UUID> {

}
