package com.kernotec.farm.inventory.rest.mapper.chip;

import com.kernotec.farm.inventory.jpa.entity.Chip;
import com.kernotec.farm.inventory.rest.dto.response.chip.ChipResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ChipResponseFlatMapper {

    @Mapping(target = "operator", ignore = true)
    @Mapping(target = "registrationPerson", ignore = true)
    @Mapping(target = "device", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    ChipResponse toResponse(Chip chip);

    ChipResponse toResponse(UUID id);

    List<ChipResponse> toResponse(List<Chip> chipList);

    Set<ChipResponse> toResponse(Set<Chip> chipSet);
}
