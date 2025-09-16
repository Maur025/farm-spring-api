package com.kernotec.farm.account.command.device.account;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.DeviceAccount;
import com.kernotec.farm.account.jpa.service.DeviceAccountService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeviceAccountCreateCmd extends
    AbstractTransactionalRequiredCommand<DeviceAccountCreateCmd.Request, UUID>
{

    private final DeviceAccountService deviceAccountService;

    @Override
    protected UUID run(Request request) {
        DeviceAccount deviceAccount = new DeviceAccount();

        deviceAccount.setDeviceId(request.getDeviceId());
        deviceAccount.setAccountId(request.getAccountId());

        deviceAccount = deviceAccountService.save(deviceAccount);
        return deviceAccount.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID deviceId;
        @NotNull
        private final UUID accountId;
    }
}
