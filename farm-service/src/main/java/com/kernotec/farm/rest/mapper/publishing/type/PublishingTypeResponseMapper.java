package com.kernotec.farm.rest.mapper.publishing.type;

import com.kernotec.farm.jpa.entity.PublishingType;
import com.kernotec.farm.rest.dto.response.publishing.type.PublishingTypeResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface PublishingTypeResponseMapper {

    PublishingTypeResponse toResponse(UUID id);

    PublishingTypeResponse toResponse(PublishingType publishingType);

    List<PublishingTypeResponse> toResponse(List<PublishingType> publishingTypeList);

    Set<PublishingTypeResponse> toResponse(Set<PublishingType> publishingTypeSet);
}
