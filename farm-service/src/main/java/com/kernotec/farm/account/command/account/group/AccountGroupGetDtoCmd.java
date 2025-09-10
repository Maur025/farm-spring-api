package com.kernotec.farm.account.command.account.group;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.dto.entity.AccountGroupDto;
import com.kernotec.farm.account.jpa.dto.mapper.AccountGroupDtoMapper;
import com.kernotec.farm.account.jpa.entity.AccountGroup;
import com.kernotec.farm.account.jpa.service.AccountGroupService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountGroupGetDtoCmd extends
    AbstractTransactionalRequiredCommand<AccountGroupGetDtoCmd.Request, AccountGroupDto>
{

    private final AccountGroupService accountGroupService;
    private final AccountGroupDtoMapper accountGroupDtoMapper;

    @Override
    protected AccountGroupDto run(Request request) {
        AccountGroup accountGroup = accountGroupService.findByIdThrow(request.getAccountGroupId());
        return accountGroupDtoMapper.toDto(accountGroup);
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID accountGroupId;
    }
}
