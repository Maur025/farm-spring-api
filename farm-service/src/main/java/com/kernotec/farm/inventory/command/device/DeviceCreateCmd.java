package com.kernotec.farm.inventory.command.device;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
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
public class DeviceCreateCmd extends
    AbstractTransactionalRequiredCommand<DeviceCreateCmd.Request, UUID>
{

    private final DeviceService deviceService;

    @Override
    protected UUID run(Request request) {
        Device device = new Device();

        device.setName(request.getName());
        device.setDeviceNumber(request.getDeviceNumber());
        device.setDeviceNumberLong(Long.valueOf(request.getDeviceNumber()
            .trim()));
        device.setModel(request.getModel());
        device.setBrand(request.getBrand());
        device.setSerialNumber(request.getSerialNumber());
        device.setFarmId(request.getFarmId());

        device = deviceService.save(device);
        return device.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String name;
        @NotNull
        private final String deviceNumber;
        private final String model;
        private final String brand;
        private final String serialNumber;
        @NotNull
        private final UUID farmId;
    }
}
