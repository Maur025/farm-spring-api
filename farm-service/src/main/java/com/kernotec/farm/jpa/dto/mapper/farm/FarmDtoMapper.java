package com.kernotec.farm.jpa.dto.mapper.farm;

import com.kernotec.farm.jpa.dto.entity.farm.FarmDto;
import com.kernotec.farm.jpa.entity.Farm;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface FarmDtoMapper {

    FarmDto toDto(Farm farm);

    List<FarmDto> toDto(List<Farm> farmList);

    Set<FarmDto> toDto(Set<Farm> farmSet);
}
