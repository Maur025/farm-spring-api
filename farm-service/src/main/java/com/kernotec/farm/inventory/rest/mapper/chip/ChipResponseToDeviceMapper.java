package com.kernotec.farm.inventory.rest.mapper.chip;

import com.kernotec.farm.account.rest.mapper.account.AccountResponseFlatMapper;
import com.kernotec.farm.inventory.jpa.entity.Chip;
import com.kernotec.farm.inventory.rest.dto.response.chip.ChipResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {AccountResponseFlatMapper.class})
public interface ChipResponseToDeviceMapper {

    @Mapping(target = "device", ignore = true)
    ChipResponse toResponse(Chip chip);

    ChipResponse toResponse(UUID id);

    List<ChipResponse> toResponse(List<Chip> chipList);

    Set<ChipResponse> toResponse(Set<Chip> chipSet);
}
