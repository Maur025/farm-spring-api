package com.kernotec.farm.activity.rest.mapper.follow;

import com.kernotec.farm.activity.jpa.entity.Follow;
import com.kernotec.farm.activity.rest.dto.response.follow.FollowResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FollowResponseForActivityMapper {

    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    FollowResponse toResponse(Follow follow);

    FollowResponse toResponse(UUID id);

    List<FollowResponse> toResponse(List<Follow> followList);

    Set<FollowResponse> toResponse(Set<Follow> followSet);
}
