package com.kernotec.farm.account.command.account.extension;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.AccountExtension;
import com.kernotec.farm.account.jpa.service.AccountExtensionService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountExtensionCreateCmd extends
    AbstractTransactionalRequiredCommand<AccountExtensionCreateCmd.Request, UUID>
{

    private final AccountExtensionService accountExtensionService;

    @Override
    protected UUID run(Request request) {
        AccountExtension accountExtension = new AccountExtension();

        accountExtension.setReferenceEmail(request.getReferenceEmail());
        accountExtension.setAccountId(request.getAccountId());

        accountExtension = accountExtensionService.save(accountExtension);
        return accountExtension.getId();
    }

    @Builder
    @Getter
    public static class Request {

        private final String referenceEmail;
        @NotNull
        private final UUID accountId;
    }
}
