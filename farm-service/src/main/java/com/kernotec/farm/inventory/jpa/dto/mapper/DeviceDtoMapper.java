package com.kernotec.farm.inventory.jpa.dto.mapper;

import com.kernotec.farm.inventory.jpa.dto.entity.DeviceDto;
import com.kernotec.farm.inventory.jpa.entity.Device;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface DeviceDtoMapper {

    DeviceDto toDto(Device device);

    List<DeviceDto> toDto(List<Device> deviceList);

    Set<DeviceDto> toDto(Set<Device> deviceSet);
}
