package com.kernotec.farm.activity.jpa.dto.mapper;

import com.kernotec.farm.activity.jpa.dto.entity.ActivityDto;
import com.kernotec.farm.activity.jpa.entity.Activity;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ActivityDtoFlatMapper {

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    @Mapping(target = "publishings", ignore = true)
    @Mapping(target = "reactions", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "groupMemberships", ignore = true)
    @Mapping(target = "connections", ignore = true)
    @Mapping(target = "follows", ignore = true)
    ActivityDto toDto(Activity activity);

    List<ActivityDto> toDto(List<Activity> activityList);

    Set<ActivityDto> toDto(Set<Activity> activitySet);
}
