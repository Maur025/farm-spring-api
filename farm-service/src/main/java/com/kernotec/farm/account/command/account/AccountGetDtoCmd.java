package com.kernotec.farm.account.command.account;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.account.jpa.dto.mapper.AccountDtoMapper;
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
public class AccountGetDtoCmd extends
    AbstractTransactionalRequiredCommand<AccountGetDtoCmd.Request, AccountDto>
{

    private final AccountService accountService;
    private final AccountDtoMapper accountDtoMapper;

    @Override
    protected AccountDto run(Request request) {
        Account account = accountService.findByIdThrow(request.getAccountId());
        return accountDtoMapper.toDto(account);
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID accountId;
    }
}
