package com.kernotec.farm.inventory.rest.mapper.farm;

import com.kernotec.farm.inventory.jpa.entity.Farm;
import com.kernotec.farm.inventory.rest.dto.response.farm.FarmResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FarmResponseMinMapper {

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "type", ignore = true)
    FarmResponse toResponse(Farm farm);

    List<FarmResponse> toResponse(List<Farm> farmList);

    Set<FarmResponse> toResponse(Set<Farm> farmSet);
}
