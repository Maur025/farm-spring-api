package com.kernotec.farm.activity.rest.mapper.group;

import com.kernotec.farm.activity.jpa.entity.Group;
import com.kernotec.farm.activity.rest.dto.response.group.GroupResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface GroupResponseForAccountMapper {

    @Mapping(target = "groupMemberships", ignore = true)
    GroupResponse toResponse(Group group);

    GroupResponse toResponse(UUID id);

    List<GroupResponse> toResponse(List<Group> groupList);

    Set<GroupResponse> toResponse(Set<Group> groupSet);
}
