package com.kernotec.farm.inventory.command.device.imei;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.inventory.jpa.entity.DeviceImei;
import com.kernotec.farm.inventory.jpa.service.DeviceImeiService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeviceImeiCreateCmd extends
    AbstractTransactionalRequiredCommand<DeviceImeiCreateCmd.Request, UUID>
{

    private final DeviceImeiService deviceImeiService;

    @Override
    protected UUID run(Request request) {
        DeviceImei deviceImei = new DeviceImei();

        deviceImei.setImei(request.getImei());
        deviceImei.setDeviceId(request.getDeviceId());

        deviceImei = deviceImeiService.save(deviceImei);
        return deviceImei.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String imei;
        @NotNull
        private final UUID deviceId;
    }
}
