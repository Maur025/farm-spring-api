package com.kernotec.farm.account.rest.command.account.group;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.account.group.AccountGroupCreateCmd;
import com.kernotec.farm.activity.jpa.enums.GroupActionEnum;
import com.kernotec.farm.parametric.jpa.entity.GroupState;
import com.kernotec.farm.parametric.jpa.enums.GroupStateCodeEnum;
import com.kernotec.farm.parametric.jpa.service.GroupStateService;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountGroupCreateInActionJoinCmd extends
    AbstractTransactionalRequiredCommand<AccountGroupCreateInActionJoinCmd.Request, Void>
{

    private final GroupStateService groupStateService;
    private final AccountGroupCreateCmd accountGroupCreateCmd;

    @Override
    protected Void run(Request request) {
        if (!request.getAction()
            .equals(GroupActionEnum.JOIN))
        {
            log.debug("Action is not JOIN, skipping command.");
            return null;
        }

        GroupState groupState = groupStateService.findByCodeThrow(GroupStateCodeEnum.JOINED);

        accountGroupCreateCmd.withRequest(AccountGroupCreateCmd.Request.builder()
                .joinedAt(request.getJoinedAt())
                .accountId(request.getAccountId())
                .groupId(request.getGroupId())
                .groupStateId(groupState.getId())
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final GroupActionEnum action;

        private final ZonedDateTime joinedAt;
        private final UUID accountId;
        private final UUID groupId;
    }
}
