package com.kernotec.farm.account.command.account.extension;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.AccountExtension;
import com.kernotec.farm.account.jpa.service.AccountExtensionService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountExtensionCreateManyCmd extends
    AbstractTransactionalRequiredCommand<AccountExtensionCreateManyCmd.Request, List<AccountExtension>>
{

    private final AccountExtensionService accountExtensionService;

    @Override
    protected List<AccountExtension> run(Request request) {
        if (request.accountExtensionList.isEmpty()) {
            log.debug("No found account extension to create");
            return null;
        }

        return accountExtensionService.saveAll(request.accountExtensionList);
    }

    @Builder
    public record Request(@NotNull List<AccountExtension> accountExtensionList) {

    }
}
