package com.kernotec.farm.command.device;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.inventory.jpa.dto.entity.DeviceDto;
import com.kernotec.farm.inventory.jpa.dto.mapper.DeviceDtoMapper;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.jpa.service.DeviceService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeviceGetDtoCmd extends
    AbstractTransactionalRequiredCommand<DeviceGetDtoCmd.Request, DeviceDto>
{

    private final DeviceService deviceService;
    private final DeviceDtoMapper deviceDtoMapper;

    @Override
    protected DeviceDto run(Request request) {
        Device device = deviceService.findByIdThrow(request.getDeviceId());
        return deviceDtoMapper.toDto(device);
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID deviceId;
    }
}
