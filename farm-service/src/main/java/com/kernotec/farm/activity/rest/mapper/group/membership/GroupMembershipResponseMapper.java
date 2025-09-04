package com.kernotec.farm.activity.rest.mapper.group.membership;

import com.kernotec.farm.activity.jpa.entity.GroupMembership;
import com.kernotec.farm.activity.rest.dto.response.group.membership.GroupMembershipResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface GroupMembershipResponseMapper {

    GroupMembershipResponse toResponse(UUID id);

    GroupMembershipResponse toResponse(GroupMembership groupMembership);

    List<GroupMembershipResponse> toResponse(List<GroupMembership> groupMembershipList);

    Set<GroupMembershipResponse> toResponse(Set<GroupMembership> groupMembershipSet);
}
