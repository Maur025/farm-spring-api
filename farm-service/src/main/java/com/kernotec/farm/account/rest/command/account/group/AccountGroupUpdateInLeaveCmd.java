package com.kernotec.farm.account.rest.command.account.group;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.account.group.AccountGroupUpdateCmd;
import com.kernotec.farm.account.jpa.entity.AccountGroup;
import com.kernotec.farm.account.jpa.service.AccountGroupService;
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
public class AccountGroupUpdateInLeaveCmd extends
    AbstractTransactionalRequiredCommand<AccountGroupUpdateInLeaveCmd.Request, Void>
{

    private final AccountGroupService accountGroupService;
    private final GroupStateService groupStateService;
    private final AccountGroupUpdateCmd accountGroupUpdateCmd;

    @Override
    protected Void run(Request request) {
        if (!request.getAction()
            .equals(GroupActionEnum.LEAVE))
        {
            log.debug("No action needed, action is not LEAVE");
            return null;
        }

        GroupState groupJoinedState = groupStateService.findByCodeThrow(GroupStateCodeEnum.JOINED);

        AccountGroup accountGroup = accountGroupService.findByAccountIdAndGroupIdAndGroupStateIdThrow(
            request.getAccountId(), request.getGroupId(), groupJoinedState.getId());

        GroupState groupLeftState = groupStateService.findByCodeThrow(GroupStateCodeEnum.LEFT);

        accountGroupUpdateCmd.withRequest(AccountGroupUpdateCmd.Request.builder()
                .accountGroupId(accountGroup.getId())
                .leftAt(request.getLeftAt())
                .groupStateId(groupLeftState.getId())
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final GroupActionEnum action;

        private final ZonedDateTime leftAt;
        private final UUID accountId;
        private final UUID groupId;
    }
}
