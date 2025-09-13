package com.kernotec.farm.activity.rest.mapper.group.membership;

import com.kernotec.farm.activity.jpa.entity.GroupMembership;
import com.kernotec.farm.activity.rest.dto.response.group.membership.GroupMembershipResponse;
import com.kernotec.farm.activity.rest.mapper.group.GroupResponseForAccountMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {GroupResponseForAccountMapper.class})
public interface GroupMemberResponseForActivityMapper {

    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    GroupMembershipResponse toResponse(GroupMembership groupMembership);

    GroupMembershipResponse toResponse(UUID id);

    List<GroupMembershipResponse> toResponse(List<GroupMembership> groupMembershipList);

    Set<GroupMembershipResponse> toResponse(Set<GroupMembership> groupMembershipSet);
}
