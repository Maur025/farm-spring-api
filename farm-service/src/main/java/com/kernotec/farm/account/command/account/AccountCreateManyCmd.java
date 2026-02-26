package com.kernotec.farm.account.command.account;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.service.AccountService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountCreateManyCmd extends
    AbstractTransactionalRequiredCommand<AccountCreateManyCmd.Request, List<Account>>
{

    private final AccountService accountService;

    @Override
    protected List<Account> run(Request request) {
        if (request.accountList.isEmpty()) {
            log.debug("No found accounts to create");
            return null;
        }

        return accountService.saveAll(request.accountList);
    }

    @Builder
    public record Request(@NotNull List<Account> accountList) {

    }
}
