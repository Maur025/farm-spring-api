package com.kernotec.farm.parametric.jpa.dto.mapper;

import com.kernotec.farm.parametric.jpa.dto.entity.ActivityTypeDto;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ActivityTypeDtoFlatMapper {

    @Mapping(target = "socialNetworks", ignore = true)
    ActivityTypeDto toDto(ActivityType activityType);

    List<ActivityTypeDto> toDto(List<ActivityType> activityTypeList);

    Set<ActivityTypeDto> toDto(Set<ActivityType> activityTypeSet);
}
