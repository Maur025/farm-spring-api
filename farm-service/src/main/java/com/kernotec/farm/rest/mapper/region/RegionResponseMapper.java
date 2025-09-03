package com.kernotec.farm.rest.mapper.region;

import com.kernotec.farm.parametric.jpa.entity.Region;
import com.kernotec.farm.rest.dto.response.region.RegionResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface RegionResponseMapper {

    RegionResponse toResponse(UUID id);

    RegionResponse toResponse(Region region);

    List<RegionResponse> toResponse(List<Region> regionList);

    Set<RegionResponse> toResponse(Set<Region> regionSet);
}
