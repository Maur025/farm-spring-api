package com.kernotec.farm.account.command.account;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.account.jpa.service.AccountService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountCreateCmd extends
    AbstractTransactionalRequiredCommand<AccountCreateCmd.Request, UUID>
{

    private final AccountService accountService;

    @Override
    protected UUID run(Request request) {
        Account account = new Account();

        account.setUsername(request.getUsername());
        account.setPassword(request.getPassword());
        account.setPersonId(request.getPersonId());
        account.setSocialNetworkId(request.getSocialNetworkId());
        account.setType(request.getType());

        account = accountService.save(account);
        return account.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String username;
        @NotNull
        private final String password;
        private final UUID personId;
        @NotNull
        private final UUID socialNetworkId;
        @NotNull
        private final AccountTypeEnum type;
    }
}
