package com.kernotec.farm.inventory.command.device.connection;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.inventory.jpa.entity.DeviceConnection;
import com.kernotec.farm.inventory.jpa.service.DeviceConnectionService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceConnectionCreateManyCmd extends
    AbstractTransactionalRequiredCommand<DeviceConnectionCreateManyCmd.Request, List<DeviceConnection>>
{

    private final DeviceConnectionService deviceConnectionService;

    @Override
    protected List<DeviceConnection> run(Request request) {
        if (request.deviceConnectionList.isEmpty()) {
            log.debug("No found device connection to save");
            return null;
        }

        return deviceConnectionService.saveAll(request.deviceConnectionList);
    }

    @Builder
    public record Request(@NotNull List<DeviceConnection> deviceConnectionList) {

    }
}
