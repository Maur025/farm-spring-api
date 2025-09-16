package com.kernotec.farm.inventory.rest.mapper.device;

import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.rest.dto.response.device.DeviceResponse;
import com.kernotec.farm.inventory.rest.mapper.chip.ChipResponseToDeviceMapper;
import com.kernotec.farm.inventory.rest.mapper.device.connection.DeviceConnectionResponseFlatMapper;
import com.kernotec.farm.inventory.rest.mapper.device.imei.DeviceImeiResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ChipResponseToDeviceMapper.class, DeviceImeiResponseFlatMapper.class,
    DeviceConnectionResponseFlatMapper.class})
public interface DeviceResponseMapper {

    DeviceResponse toResponse(UUID id);

    @Mapping(target = "accounts", ignore = true)
    DeviceResponse toResponse(Device device);

    List<DeviceResponse> toResponse(List<Device> deviceList);

    Set<DeviceResponse> toResponse(Set<Device> deviceSet);
}
