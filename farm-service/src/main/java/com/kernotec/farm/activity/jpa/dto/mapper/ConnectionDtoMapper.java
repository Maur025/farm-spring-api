package com.kernotec.farm.activity.jpa.dto.mapper;

import com.kernotec.farm.account.jpa.dto.mapper.AccountDtoFlatMapper;
import com.kernotec.farm.activity.jpa.dto.entity.ConnectionDto;
import com.kernotec.farm.activity.jpa.entity.Connection;
import com.kernotec.farm.parametric.jpa.dto.mapper.ActivityTypeDtoFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {AccountDtoFlatMapper.class, ActivityDtoFlatMapper.class,
    ActivityTypeDtoFlatMapper.class})
public interface ConnectionDtoMapper {

    ConnectionDto toDto(Connection connection);

    List<ConnectionDto> toDto(List<Connection> connectionList);

    Set<ConnectionDto> toDto(Set<Connection> connectionSet);
}
