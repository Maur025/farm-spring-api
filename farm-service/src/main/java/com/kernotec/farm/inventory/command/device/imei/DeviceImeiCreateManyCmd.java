package com.kernotec.farm.inventory.command.device.imei;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.inventory.jpa.entity.DeviceImei;
import com.kernotec.farm.inventory.jpa.service.DeviceImeiService;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceImeiCreateManyCmd extends
    AbstractTransactionalRequiredCommand<DeviceImeiCreateManyCmd.Request, List<DeviceImei>>
{

    private final DeviceImeiService deviceImeiService;

    @Override
    protected List<DeviceImei> run(Request request) {
        if (request.deviceImeiList.isEmpty()) {
            log.debug("No found device imei to save");
            return null;
        }

        return deviceImeiService.saveAll(request.deviceImeiList);
    }

    @Builder
    public record Request(List<DeviceImei> deviceImeiList) {

    }
}
