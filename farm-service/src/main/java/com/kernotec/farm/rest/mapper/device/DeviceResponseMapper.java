package com.kernotec.farm.rest.mapper.device;

import com.kernotec.farm.jpa.entity.Device;
import com.kernotec.farm.rest.dto.response.device.DeviceResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface DeviceResponseMapper {

    DeviceResponse toResponse(UUID id);

    DeviceResponse toResponse(Device device);

    List<DeviceResponse> toResponse(List<Device> deviceList);

    Set<DeviceResponse> toResponse(Set<Device> deviceSet);
}
