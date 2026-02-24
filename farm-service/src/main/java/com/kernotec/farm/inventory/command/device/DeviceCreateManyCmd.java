package com.kernotec.farm.inventory.command.device;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.jpa.service.DeviceService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceCreateManyCmd extends
    AbstractTransactionalRequiredCommand<DeviceCreateManyCmd.Request, List<Device>>
{

    private final DeviceService deviceService;

    @Override
    protected List<Device> run(Request request) {
        if (request.deviceList.isEmpty()) {
            log.debug("No found devices to save");
            return null;
        }

        return deviceService.saveAll(request.deviceList);
    }

    @Builder
    public record Request(@NotNull List<Device> deviceList) {

    }
}
