package com.kernotec.farm.account.command.account;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.service.AccountService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
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
        Account account = accountService.findByIdThrow(request.accountId());

        if (request.username() != null) {
            account.setUsername(request.username());
        }
        if (request.password() != null) {
            account.setPassword(request.password());
        }
        if (request.personId() != null) {
            account.setPersonId(request.personId());
        }
        if (request.isEnabled() != null) {
            account.setIsEnabled(request.isEnabled());
        }
        if (request.accountLink() != null) {
            account.setAccountLink(request.accountLink());
        }
        if (request.identityUsername() != null) {
            account.setIdentityUsername(request.identityUsername());
        }

        accountService.save(account);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID accountId, String username, String password, UUID personId,
                          Boolean isEnabled, String accountLink, String identityUsername)
    {

    }
}
