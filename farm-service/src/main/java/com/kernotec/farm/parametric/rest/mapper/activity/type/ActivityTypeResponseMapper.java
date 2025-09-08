package com.kernotec.farm.parametric.rest.mapper.activity.type;

import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.parametric.rest.dto.response.activity.type.ActivityTypeResponse;
import com.kernotec.farm.parametric.rest.mapper.social.network.SocialNetworkResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {SocialNetworkResponseFlatMapper.class})
public interface ActivityTypeResponseMapper {

    ActivityTypeResponse toResponse(UUID id);

    ActivityTypeResponse toResponse(ActivityType activityType);

    List<ActivityTypeResponse> toResponse(List<ActivityType> activityTypeList);

    Set<ActivityTypeResponse> toResponse(Set<ActivityType> activityTypeSet);
}
