package com.kernotec.farm.activity.rest.mapper.publishing;

import com.kernotec.farm.activity.jpa.entity.Publishing;
import com.kernotec.farm.activity.rest.dto.response.publishing.PublishingResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PublishingResponseForActivityMapper {

    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    PublishingResponse toResponse(Publishing publishing);

    PublishingResponse toResponse(UUID id);

    List<PublishingResponse> toResponse(List<Publishing> publishingList);

    Set<PublishingResponse> toResponse(Set<Publishing> publishingSet);
}
