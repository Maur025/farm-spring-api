package com.kernotec.farm.inventory.jpa.dto.mapper;

import com.kernotec.farm.inventory.jpa.dto.entity.ChipDto;
import com.kernotec.farm.inventory.jpa.entity.Chip;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ChipDtoFlatMapper {

    @Mapping(target = "operator", ignore = true)
    @Mapping(target = "registrationPerson", ignore = true)
    @Mapping(target = "device", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    ChipDto toDto(Chip chip);

    List<ChipDto> toDto(List<Chip> chipList);

    Set<ChipDto> toDto(Set<Chip> chipSet);
}
