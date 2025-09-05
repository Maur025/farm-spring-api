package com.kernotec.farm.activity.jpa.dto.mapper;

import com.kernotec.farm.activity.jpa.dto.entity.GroupMembershipDto;
import com.kernotec.farm.activity.jpa.entity.GroupMembership;
import com.kernotec.farm.parametric.jpa.dto.mapper.ActivityTypeDtoFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(
    uses = {GroupDtoFlatMapper.class, ActivityDtoFlatMapper.class, ActivityTypeDtoFlatMapper.class})
public interface GroupMembershipDtoMapper {

    GroupMembershipDto toDto(GroupMembership groupMembership);

    List<GroupMembershipDto> toDto(List<GroupMembership> groupMembershipList);

    Set<GroupMembershipDto> toDto(Set<GroupMembership> groupMembershipSet);
}
