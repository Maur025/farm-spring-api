package com.kernotec.farm.account.command.account.group;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.AccountGroup;
import com.kernotec.farm.account.jpa.service.AccountGroupService;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountGroupCreateCmd extends
    AbstractTransactionalRequiredCommand<AccountGroupCreateCmd.Request, UUID>
{

    private final AccountGroupService accountGroupService;

    @Override
    protected UUID run(Request request) {
        AccountGroup accountGroup = new AccountGroup();

        accountGroup.setJoinedAt(request.getJoinedAt());
        accountGroup.setLeftAt(request.getLeftAt());
        accountGroup.setAccountId(request.getAccountId());
        accountGroup.setGroupId(request.getGroupId());
        accountGroup.setGroupStateId(request.getGroupStateId());

        accountGroup = accountGroupService.save(accountGroup);
        return accountGroup.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final ZonedDateTime joinedAt;
        private final ZonedDateTime leftAt;
        @NotNull
        private final UUID accountId;
        @NotNull
        private final UUID groupId;
        @NotNull
        private final UUID groupStateId;
    }
}
