package com.kernotec.farm.rest.mapper.publishing.context;

import com.kernotec.farm.jpa.entity.PublishingContext;
import com.kernotec.farm.rest.dto.response.publishing.context.PublishingContextResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface PublishingContextResponseMapper {

    PublishingContextResponse toResponse(UUID id);

    PublishingContextResponse toResponse(PublishingContext publishingContext);

    List<PublishingContextResponse> toResponse(List<PublishingContext> publishingContextList);

    Set<PublishingContextResponse> toResponse(Set<PublishingContext> publishingContextSet);
}
