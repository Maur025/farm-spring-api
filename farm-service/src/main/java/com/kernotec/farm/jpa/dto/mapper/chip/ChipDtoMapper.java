package com.kernotec.farm.jpa.dto.mapper.chip;

import com.kernotec.farm.jpa.dto.entity.chip.ChipDto;
import com.kernotec.farm.jpa.entity.Chip;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface ChipDtoMapper {

    ChipDto toDto(Chip chip);

    List<ChipDto> toDto(List<Chip> chipList);

    Set<ChipDto> toDto(Set<Chip> chipSet);
}
