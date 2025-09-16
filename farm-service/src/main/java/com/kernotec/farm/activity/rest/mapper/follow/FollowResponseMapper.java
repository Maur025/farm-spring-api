package com.kernotec.farm.activity.rest.mapper.follow;

import com.kernotec.farm.activity.jpa.entity.Follow;
import com.kernotec.farm.activity.rest.dto.response.follow.FollowResponse;
import com.kernotec.farm.activity.rest.mapper.activity.ActivityResponseFlatMapper;
import com.kernotec.farm.parametric.rest.mapper.activity.type.ActivityTypeResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {ActivityResponseFlatMapper.class, ActivityTypeResponseFlatMapper.class})
public interface FollowResponseMapper {

    FollowResponse toResponse(Follow follow);

    FollowResponse toResponse(UUID id);

    List<FollowResponse> toResponse(List<Follow> followList);

    Set<FollowResponse> toResponse(Set<Follow> followSet);
}
