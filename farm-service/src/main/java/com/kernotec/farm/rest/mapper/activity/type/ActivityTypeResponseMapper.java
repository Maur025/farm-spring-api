package com.kernotec.farm.rest.mapper.activity.type;

import com.kernotec.farm.jpa.entity.ActivityType;
import com.kernotec.farm.rest.dto.response.activity.type.ActivityTypeResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface ActivityTypeResponseMapper {

    ActivityTypeResponse toResponse(UUID id);

    ActivityTypeResponse toResponse(ActivityType activityType);

    List<ActivityTypeResponse> toResponse(List<ActivityType> activityTypeList);

    Set<ActivityTypeResponse> toResponse(Set<ActivityType> activityTypeSet);
}
