package com.kernotec.farm.activity.jpa.dto.mapper;

import com.kernotec.farm.activity.jpa.dto.entity.GroupDto;
import com.kernotec.farm.activity.jpa.entity.Group;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {GroupMembershipDtoFlatMapper.class})
public interface GroupDtoMapper {

    GroupDto toDto(Group group);

    List<GroupDto> toDto(List<Group> groupList);

    Set<GroupDto> toDto(Set<Group> groupSet);
}

