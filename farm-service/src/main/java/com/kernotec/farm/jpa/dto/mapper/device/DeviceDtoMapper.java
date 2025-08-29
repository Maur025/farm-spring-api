package com.kernotec.farm.jpa.dto.mapper.device;

import com.kernotec.farm.jpa.dto.entity.device.DeviceDto;
import com.kernotec.farm.jpa.entity.Device;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface DeviceDtoMapper {

    DeviceDto toDto(Device device);

    List<DeviceDto> toDto(List<Device> deviceList);

    Set<DeviceDto> toDto(Set<Device> deviceSet);
}
