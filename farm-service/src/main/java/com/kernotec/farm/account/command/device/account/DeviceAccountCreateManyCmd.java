package com.kernotec.farm.account.command.device.account;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.DeviceAccount;
import com.kernotec.farm.account.jpa.service.DeviceAccountService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceAccountCreateManyCmd extends
    AbstractTransactionalRequiredCommand<DeviceAccountCreateManyCmd.Request, List<DeviceAccount>>
{

    private final DeviceAccountService deviceAccountService;

    @Override
    protected List<DeviceAccount> run(Request request) {
        if (request.deviceAccountList.isEmpty()) {
            log.debug("No found device accounts to create.");
            return null;
        }

        return deviceAccountService.saveAll(request.deviceAccountList);
    }

    @Builder
    public record Request(@NotNull List<DeviceAccount> deviceAccountList) {

    }
}
