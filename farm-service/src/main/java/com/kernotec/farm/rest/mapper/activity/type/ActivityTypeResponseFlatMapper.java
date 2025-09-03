package com.kernotec.farm.rest.mapper.activity.type;

import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.rest.dto.response.activity.type.ActivityTypeResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ActivityTypeResponseFlatMapper {

    ActivityTypeResponse toResponse(UUID id);

    @Mapping(target = "socialNetworks", ignore = true)
    ActivityTypeResponse toResponse(ActivityType activityType);

    List<ActivityTypeResponse> toResponse(List<ActivityType> activityTypeList);

    Set<ActivityTypeResponse> toResponse(Set<ActivityType> activityTypeSet);
}
