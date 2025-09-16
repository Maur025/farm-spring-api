package com.kernotec.farm.inventory.rest.mapper.device;

import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.rest.dto.response.device.DeviceResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DeviceResponseFlatMapper {

    @Mapping(target = "farm", ignore = true)
    @Mapping(target = "chips", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "deviceImeis", ignore = true)
    @Mapping(target = "deviceConnections", ignore = true)
    DeviceResponse toResponse(Device device);

    DeviceResponse toResponse(UUID id);

    List<DeviceResponse> toResponse(List<Device> deviceList);

    Set<DeviceResponse> toResponse(Set<Device> deviceSet);
}
