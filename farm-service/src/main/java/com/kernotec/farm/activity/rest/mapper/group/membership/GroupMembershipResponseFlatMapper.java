package com.kernotec.farm.activity.rest.mapper.group.membership;

import com.kernotec.farm.activity.jpa.entity.GroupMembership;
import com.kernotec.farm.activity.rest.dto.response.group.membership.GroupMembershipResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface GroupMembershipResponseFlatMapper {

    @Mapping(target = "group", ignore = true)
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "requestState", ignore = true)
    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    GroupMembershipResponse toResponse(GroupMembership groupMembership);

    GroupMembershipResponse toResponse(UUID id);

    List<GroupMembershipResponse> toResponse(List<GroupMembership> groupMembershipList);

    Set<GroupMembershipResponse> toResponse(Set<GroupMembership> groupMembershipSet);
}
