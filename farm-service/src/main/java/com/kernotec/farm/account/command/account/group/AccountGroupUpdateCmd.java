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
public class AccountGroupUpdateCmd extends
    AbstractTransactionalRequiredCommand<AccountGroupUpdateCmd.Request, Void>
{

    private final AccountGroupService accountGroupService;

    @Override
    protected Void run(Request request) {
        AccountGroup accountGroup = accountGroupService.findByIdThrow(request.getAccountGroupId());

        if (request.getLeftAt() != null) {
            accountGroup.setLeftAt(request.getLeftAt());
        }

        if (request.getGroupStateId() != null) {
            accountGroup.setGroupStateId(request.getGroupStateId());
        }

        accountGroupService.save(accountGroup);
        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID accountGroupId;

        private final ZonedDateTime leftAt;
        private final UUID groupStateId;
    }
}
