package com.kernotec.farm.rest.mapper.group;

import com.kernotec.farm.activity.jpa.entity.Group;
import com.kernotec.farm.rest.dto.response.group.GroupResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface GroupResponseMapper {

    GroupResponse toResponse(UUID id);

    GroupResponse toResponse(Group group);

    List<GroupResponse> toResponse(List<Group> groupList);

    Set<GroupResponse> toResponse(Set<Group> groupSet);
}
