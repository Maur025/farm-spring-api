package com.kernotec.farm.inventory.rest.mapper.chip;

import com.kernotec.farm.account.rest.mapper.account.AccountResponseToDeviceMapper;
import com.kernotec.farm.inventory.jpa.entity.Chip;
import com.kernotec.farm.inventory.rest.dto.response.chip.ChipResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {AccountResponseToDeviceMapper.class})
public interface ChipResponseToFilterMapper {

    @Mapping(target = "device", ignore = true)
    ChipResponse toResponse(Chip chip, @Context UUID socialNetworkId, @Context String keyword);

    List<ChipResponse> toResponse(List<Chip> chipList, @Context UUID socialNetworkId,
        @Context String keyword);

    Set<ChipResponse> toResponse(Set<Chip> chipSet, @Context UUID socialNetworkId,
        @Context String keyword);
}
