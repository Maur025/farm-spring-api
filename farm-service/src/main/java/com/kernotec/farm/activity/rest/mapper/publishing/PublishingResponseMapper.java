package com.kernotec.farm.activity.rest.mapper.publishing;

import com.kernotec.farm.activity.jpa.entity.Publishing;
import com.kernotec.farm.activity.rest.dto.response.publishing.PublishingResponse;
import com.kernotec.farm.activity.rest.mapper.activity.ActivityResponseFlatMapper;
import com.kernotec.farm.parametric.rest.mapper.activity.type.ActivityTypeResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {ActivityResponseFlatMapper.class, ActivityTypeResponseFlatMapper.class})
public interface PublishingResponseMapper {

    PublishingResponse toResponse(Publishing publishing);

    PublishingResponse toResponse(UUID id);

    List<PublishingResponse> toResponse(List<Publishing> publishingList);

    Set<PublishingResponse> toResponse(Set<Publishing> publishingSet);
}
