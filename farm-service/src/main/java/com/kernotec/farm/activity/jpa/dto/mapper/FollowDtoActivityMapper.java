package com.kernotec.farm.activity.jpa.dto.mapper;

import com.kernotec.farm.activity.jpa.dto.entity.FollowDto;
import com.kernotec.farm.activity.jpa.entity.Follow;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FollowDtoActivityMapper {

    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    FollowDto toDto(Follow follow);

    List<FollowDto> toDto(List<Follow> followList);

    Set<FollowDto> toDto(Set<Follow> followSet);
}
