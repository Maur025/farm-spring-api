package com.kernotec.farm.activity.jpa.dto.mapper;

import com.kernotec.farm.account.jpa.dto.mapper.AccountDtoFlatMapper;
import com.kernotec.farm.activity.jpa.dto.entity.ConnectionDto;
import com.kernotec.farm.activity.jpa.entity.Connection;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {AccountDtoFlatMapper.class})
public interface ConnectionDtoActivityMapper {

    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    ConnectionDto toDto(Connection connection);

    List<ConnectionDto> toDto(List<Connection> connectionList);

    Set<ConnectionDto> toDto(Set<Connection> connectionSet);
}
