package com.kernotec.farm.activity.rest.mapper.connection;

import com.kernotec.farm.activity.jpa.entity.Connection;
import com.kernotec.farm.activity.rest.dto.response.connection.ConnectionResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface ConnectionResponseMapper {

    ConnectionResponse toResponse(UUID id);

    ConnectionResponse toResponse(Connection connection);

    List<ConnectionResponse> toResponse(List<Connection> connectionList);

    Set<ConnectionResponse> toResponse(Set<Connection> connectionSet);
}
