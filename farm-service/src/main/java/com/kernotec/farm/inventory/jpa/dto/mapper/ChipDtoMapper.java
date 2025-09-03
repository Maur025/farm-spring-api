package com.kernotec.farm.inventory.jpa.dto.mapper;

import com.kernotec.farm.inventory.jpa.dto.entity.ChipDto;
import com.kernotec.farm.inventory.jpa.entity.Chip;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface ChipDtoMapper {

    ChipDto toDto(Chip chip);

    List<ChipDto> toDto(List<Chip> chipList);

    Set<ChipDto> toDto(Set<Chip> chipSet);
}
