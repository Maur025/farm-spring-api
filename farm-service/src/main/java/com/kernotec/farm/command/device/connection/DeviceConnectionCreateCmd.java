package com.kernotec.farm.command.device.connection;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.jpa.entity.DeviceConnection;
import com.kernotec.farm.jpa.service.DeviceConnectionService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeviceConnectionCreateCmd extends
    AbstractTransactionalRequiredCommand<DeviceConnectionCreateCmd.Request, UUID>
{

    private final DeviceConnectionService deviceConnectionService;

    @Override
    protected UUID run(Request request) {
        DeviceConnection deviceConnection = new DeviceConnection();

        deviceConnection.setMacAddress(request.getMacAddress());
        deviceConnection.setIpAddress(request.getIpAddress());
        deviceConnection.setIpGateway(request.getIpGateway());
        deviceConnection.setNetworkName(request.getNetworkName());
        deviceConnection.setDeviceId(request.getDeviceId());

        deviceConnection = deviceConnectionService.save(deviceConnection);
        return deviceConnection.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String macAddress;
        @NotNull
        private final String ipAddress;
        @NotNull
        private final String ipGateway;
        @NotNull
        private final String networkName;
        @NotNull
        private final UUID deviceId;
    }
}
