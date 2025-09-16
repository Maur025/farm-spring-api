package com.kernotec.farm.activity.rest.mapper.group.membership;

import com.kernotec.farm.activity.jpa.entity.GroupMembership;
import com.kernotec.farm.activity.rest.dto.response.group.membership.GroupMembershipResponse;
import com.kernotec.farm.activity.rest.mapper.activity.ActivityResponseFlatMapper;
import com.kernotec.farm.activity.rest.mapper.group.GroupResponseMapper;
import com.kernotec.farm.parametric.rest.mapper.activity.type.ActivityTypeResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {GroupResponseMapper.class, ActivityResponseFlatMapper.class,
    ActivityTypeResponseFlatMapper.class})
public interface GroupMembershipResponseMapper {

    GroupMembershipResponse toResponse(GroupMembership groupMembership);

    GroupMembershipResponse toResponse(UUID id);

    List<GroupMembershipResponse> toResponse(List<GroupMembership> groupMembershipList);

    Set<GroupMembershipResponse> toResponse(Set<GroupMembership> groupMembershipSet);
}
