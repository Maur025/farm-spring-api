package com.kernotec.farm.account.command.account;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.service.AccountService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountUpdateCmd extends
    AbstractTransactionalRequiredCommand<AccountUpdateCmd.Request, Void>
{

    private final AccountService accountService;

    @Override
    protected Void run(Request request) {
        Account account = accountService.findByIdThrow(request.getAccountId());

        if (request.getUsername() != null) {
            account.setUsername(request.getUsername());
        }
        if (request.getPassword() != null) {
            account.setPassword(request.getPassword());
        }
        if (request.getIsEnabled() != null) {
            account.setIsEnabled(request.getIsEnabled());
        }

        accountService.save(account);
        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID accountId;

        private final String username;
        private final String password;

        private final Boolean isEnabled;
    }
}
