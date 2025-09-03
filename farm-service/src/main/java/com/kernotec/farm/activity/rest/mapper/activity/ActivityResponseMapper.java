package com.kernotec.farm.activity.rest.mapper.activity;

import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.rest.dto.response.activity.ActivityResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface ActivityResponseMapper {

    ActivityResponse toResponse(UUID id);

    ActivityResponse toResponse(Activity activity);

    List<ActivityResponse> toResponse(List<Activity> activityList);

    Set<ActivityResponse> toResponse(Set<Activity> activitySet);
}
