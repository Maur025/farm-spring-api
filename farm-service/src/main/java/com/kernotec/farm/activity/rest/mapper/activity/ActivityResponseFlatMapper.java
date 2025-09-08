package com.kernotec.farm.activity.rest.mapper.activity;

import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.rest.dto.response.activity.ActivityResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ActivityResponseFlatMapper {

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    @Mapping(target = "publishings", ignore = true)
    @Mapping(target = "reactions", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "groupMemberships", ignore = true)
    @Mapping(target = "connections", ignore = true)
    @Mapping(target = "follows", ignore = true)
    ActivityResponse toResponse(Activity activity);

    ActivityResponse toResponse(UUID id);

    List<ActivityResponse> toResponse(List<Activity> activityList);

    Set<ActivityResponse> toResponse(Set<Activity> activitySet);
}
