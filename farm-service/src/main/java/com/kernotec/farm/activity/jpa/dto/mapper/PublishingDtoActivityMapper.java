package com.kernotec.farm.activity.jpa.dto.mapper;

import com.kernotec.farm.activity.jpa.dto.entity.PublishingDto;
import com.kernotec.farm.activity.jpa.entity.Publishing;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PublishingDtoActivityMapper {

    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    PublishingDto toDto(Publishing publishing);

    List<PublishingDto> toDto(List<Publishing> publishingList);

    Set<PublishingDto> toDto(Set<Publishing> publishingSet);
}
