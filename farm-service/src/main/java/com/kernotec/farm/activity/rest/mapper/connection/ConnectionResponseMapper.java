package com.kernotec.farm.activity.rest.mapper.connection;

import com.kernotec.farm.account.rest.mapper.account.AccountResponseFlatMapper;
import com.kernotec.farm.activity.jpa.entity.Connection;
import com.kernotec.farm.activity.rest.dto.response.connection.ConnectionResponse;
import com.kernotec.farm.activity.rest.mapper.activity.ActivityResponseFlatMapper;
import com.kernotec.farm.parametric.rest.mapper.activity.type.ActivityTypeResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {AccountResponseFlatMapper.class, ActivityResponseFlatMapper.class,
    ActivityTypeResponseFlatMapper.class})
public interface ConnectionResponseMapper {

    ConnectionResponse toResponse(Connection connection);

    ConnectionResponse toResponse(UUID id);

    List<ConnectionResponse> toResponse(List<Connection> connectionList);

    Set<ConnectionResponse> toResponse(Set<Connection> connectionSet);
}
