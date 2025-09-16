package com.kernotec.farm.inventory.rest.mapper.device.connection;

import com.kernotec.farm.inventory.jpa.entity.DeviceConnection;
import com.kernotec.farm.inventory.rest.dto.response.device.connection.DeviceConnectionResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DeviceConnectionResponseFlatMapper {

    @Mapping(target = "device", ignore = true)
    DeviceConnectionResponse toResponse(DeviceConnection deviceConnection);

    DeviceConnectionResponse toResponse(UUID id);

    List<DeviceConnectionResponse> toResponse(List<DeviceConnection> deviceConnectionList);

    Set<DeviceConnectionResponse> toResponse(Set<DeviceConnection> deviceConnectionSet);
}
