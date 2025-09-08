package com.kernotec.farm.inventory.rest.mapper.device;

import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.rest.dto.response.device.DeviceResponse;
import com.kernotec.farm.inventory.rest.mapper.chip.ChipResponseToFilterMapper;
import com.kernotec.farm.inventory.rest.mapper.device.imei.DeviceImeiResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ChipResponseToFilterMapper.class, DeviceImeiResponseFlatMapper.class})
public interface DeviceResponseFilterMapper {

    @Mapping(target = "accounts", ignore = true)
    DeviceResponse toResponse(Device device, @Context UUID socialNetworkId,
        @Context String keyword);

    List<DeviceResponse> toResponse(List<Device> deviceList, @Context UUID socialNetworkId,
        @Context String keyword);

    Set<DeviceResponse> toResponse(Set<Device> deviceSet, @Context UUID socialNetworkId,
        @Context String keyword);
}
