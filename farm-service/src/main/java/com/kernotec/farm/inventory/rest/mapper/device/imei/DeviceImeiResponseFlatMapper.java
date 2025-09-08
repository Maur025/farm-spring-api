package com.kernotec.farm.inventory.rest.mapper.device.imei;

import com.kernotec.farm.inventory.jpa.entity.DeviceImei;
import com.kernotec.farm.inventory.rest.dto.response.device.imei.DeviceImeiResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DeviceImeiResponseFlatMapper {

    @Mapping(target = "device", ignore = true)
    DeviceImeiResponse toResponse(DeviceImei deviceImei);

    DeviceImeiResponse toResponse(UUID id);

    List<DeviceImeiResponse> toResponse(List<DeviceImei> deviceImeiList);

    Set<DeviceImeiResponse> toResponse(Set<DeviceImei> deviceImeiSet);
}
