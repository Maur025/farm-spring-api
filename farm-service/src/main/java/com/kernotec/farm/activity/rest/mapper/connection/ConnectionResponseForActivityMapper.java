package com.kernotec.farm.activity.rest.mapper.connection;

import com.kernotec.farm.account.rest.mapper.account.AccountResponseFlatMapper;
import com.kernotec.farm.activity.jpa.entity.Connection;
import com.kernotec.farm.activity.rest.dto.response.connection.ConnectionResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {AccountResponseFlatMapper.class})
public interface ConnectionResponseForActivityMapper {

    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    ConnectionResponse toResponse(Connection connection);

    ConnectionResponse toResponse(UUID id);

    List<ConnectionResponse> toResponse(List<Connection> connectionList);

    Set<ConnectionResponse> toResponse(Set<Connection> connectionSet);
}
