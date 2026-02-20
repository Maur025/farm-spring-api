package com.kernotec.farm.account.command.account.extension.log;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.AccountExtensionLog;
import com.kernotec.farm.account.jpa.service.AccountExtensionLogService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountExtensionLogCreateCmd extends
    AbstractTransactionalRequiredCommand<AccountExtensionLogCreateCmd.Request, UUID>
{

    private final AccountExtensionLogService accountExtensionLogService;

    @Override
    protected UUID run(Request request) {
        var accountExtensionLog = new AccountExtensionLog();

        accountExtensionLog.setReferenceEmailValue(request.referenceEmailValue);
        accountExtensionLog.setAccountId(request.accountId);

        accountExtensionLog = accountExtensionLogService.save(accountExtensionLog);
        return accountExtensionLog.getId();
    }

    @Builder
    public record Request(@NotNull String referenceEmailValue, @NotNull UUID accountId) {

    }
}
