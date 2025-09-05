package com.kernotec.farm.activity.jpa.dto.mapper;

import com.kernotec.farm.activity.jpa.dto.entity.GroupMembershipDto;
import com.kernotec.farm.activity.jpa.entity.GroupMembership;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface GroupMembershipDtoFlatMapper {

    @Mapping(target = "group", ignore = true)
    @Mapping(target = "publishingContext", ignore = true)
    @Mapping(target = "requestState", ignore = true)
    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    GroupMembershipDto toDto(GroupMembership groupMembership);

    List<GroupMembershipDto> toDto(List<GroupMembership> groupMembershipList);

    Set<GroupMembershipDto> toDto(Set<GroupMembership> groupMembershipSet);
}
