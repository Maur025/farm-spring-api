package com.kernotec.farm.parametric.jpa.dto.mapper;

import com.kernotec.farm.parametric.jpa.dto.entity.OperatorDto;
import com.kernotec.farm.parametric.jpa.entity.Operator;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface OperatorDtoMapper {

    OperatorDto toDto(Operator operator);

    List<OperatorDto> toDto(List<Operator> operatorList);

    Set<OperatorDto> toDto(Set<Operator> operatorSet);
}
