package com.kernotec.farm.inventory.rest.mapper.device;

import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.rest.dto.response.device.DeviceResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DeviceResponseMinMapper {

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "model", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "serialNumber", ignore = true)
    @Mapping(target = "farmId", ignore = true)
    @Mapping(target = "farm", ignore = true)
    @Mapping(target = "chips", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "deviceImeis", ignore = true)
    @Mapping(target = "deviceConnections", ignore = true)
    DeviceResponse toResponse(Device device);

    List<DeviceResponse> toResponse(List<Device> deviceList);

    Set<DeviceResponse> toResponse(Set<Device> deviceSet);
}
