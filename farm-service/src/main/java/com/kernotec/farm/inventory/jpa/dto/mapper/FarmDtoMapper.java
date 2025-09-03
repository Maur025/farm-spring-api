package com.kernotec.farm.inventory.jpa.dto.mapper;

import com.kernotec.farm.inventory.jpa.dto.entity.FarmDto;
import com.kernotec.farm.inventory.jpa.entity.Farm;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface FarmDtoMapper {

    FarmDto toDto(Farm farm);

    List<FarmDto> toDto(List<Farm> farmList);

    Set<FarmDto> toDto(Set<Farm> farmSet);
}
