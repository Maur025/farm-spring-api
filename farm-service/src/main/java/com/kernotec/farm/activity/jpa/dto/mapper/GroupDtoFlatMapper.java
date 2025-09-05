package com.kernotec.farm.activity.jpa.dto.mapper;

import com.kernotec.farm.activity.jpa.dto.entity.GroupDto;
import com.kernotec.farm.activity.jpa.entity.Group;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface GroupDtoFlatMapper {

    @Mapping(target = "region", ignore = true)
    @Mapping(target = "groupMemberships", ignore = true)
    GroupDto toResponse(Group group);

    List<GroupDto> toResponse(List<Group> groupList);

    Set<GroupDto> toResponse(Set<Group> groupSet);
}
