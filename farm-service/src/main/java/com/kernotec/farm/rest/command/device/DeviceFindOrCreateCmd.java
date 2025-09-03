package com.kernotec.farm.rest.command.device;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.command.device.DeviceCreateCmd;
import com.kernotec.farm.command.device.DeviceGetDtoCmd;
import com.kernotec.farm.command.device.connection.DeviceConnectionCreateCmd;
import com.kernotec.farm.command.device.imei.DeviceImeiCreateCmd;
import com.kernotec.farm.inventory.jpa.dto.entity.DeviceDto;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.jpa.service.DeviceService;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeviceFindOrCreateCmd extends
    AbstractTransactionalRequiredCommand<DeviceFindOrCreateCmd.Request, DeviceDto>
{

    private final DeviceGetDtoCmd deviceGetDtoCmd;
    private final DeviceService deviceService;
    private final DeviceCreateCmd deviceCreateCmd;
    private final DeviceConnectionCreateCmd deviceConnectionCreateCmd;
    private final DeviceImeiCreateCmd deviceImeiCreateCmd;

    @Override
    protected DeviceDto run(Request request) {
        Optional<Device> device = deviceService.findByDeviceNumber(request.getDeviceNumber());

        UUID deviceId = device.map(Device::getId)
            .orElseGet(() -> {
                UUID newDeviceId = deviceCreateCmd.withRequest(DeviceCreateCmd.Request.builder()
                        .name(request.getName())
                        .deviceNumber(request.getDeviceNumber())
                        .model(request.getModel() == null ? "N/A" : request.getModel())
                        .brand(request.getBrand() == null ? "N/A" : request.getBrand())
                        .serialNumber(
                            request.getSerialNumber() == null ? "N/A" : request.getSerialNumber())
                        .farmId(request.getFarmId())
                        .build())
                    .execute();

                deviceConnectionCreateCmd.withRequest(DeviceConnectionCreateCmd.Request.builder()
                        .macAddress(request.getMacAddress() == null ? "N/A" : request.getMacAddress())
                        .ipAddress(request.getIpAddress() == null ? "N/A" : request.getIpAddress())
                        .ipGateway(request.getIpGateway() == null ? "N/A" : request.getIpGateway())
                        .networkName(
                            request.getNetworkName() == null ? "N/A" : request.getNetworkName())
                        .deviceId(newDeviceId)
                        .build())
                    .execute();

                if (request.getImeiOne() == null) {
                    deviceImeiCreateCmd.withRequest(DeviceImeiCreateCmd.Request.builder()
                            .imei(request.getImeiOne())
                            .deviceId(newDeviceId)
                            .build())
                        .execute();
                }

                if (request.getImeiTwo() == null) {
                    deviceImeiCreateCmd.withRequest(DeviceImeiCreateCmd.Request.builder()
                            .imei(request.getImeiTwo())
                            .deviceId(newDeviceId)
                            .build())
                        .execute();
                }

                return newDeviceId;
            });

        return deviceGetDtoCmd.withRequest(DeviceGetDtoCmd.Request.builder()
                .deviceId(deviceId)
                .build())
            .execute();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String deviceNumber;
        private final String name;
        private final String model;
        private final String brand;
        private final String serialNumber;

        @NotNull
        private final UUID farmId;

        private final String macAddress;
        private final String ipAddress;
        private final String ipGateway;
        private final String networkName;

        private final String imeiOne;
        private final String imeiTwo;
    }
}
