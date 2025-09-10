package com.kernotec.farm.account.rest.command.account.group;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.account.group.AccountGroupGetDtoCmd;
import com.kernotec.farm.account.exception.AccountGroupException;
import com.kernotec.farm.account.jpa.dto.entity.AccountGroupDto;
import com.kernotec.farm.account.rest.dto.request.account.group.AccountGroupLeaveRequest;
import com.kernotec.farm.activity.command.activity.ActivityCreateCmd;
import com.kernotec.farm.activity.command.group.membership.GroupMembershipCreateCmd;
import com.kernotec.farm.activity.jpa.enums.GroupActionEnum;
import com.kernotec.farm.parametric.command.request.state.RequestStateGetIdByCodeCmd;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.parametric.jpa.enums.ActivityTypeCodeEnum;
import com.kernotec.farm.parametric.jpa.enums.RequestStateCodeEnum;
import com.kernotec.farm.parametric.jpa.service.ActivityTypeService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountGroupLeaveCmd extends
    AbstractTransactionalRequiredCommand<AccountGroupLeaveCmd.Request, Void>
{

    private final AccountGroupGetDtoCmd accountGroupGetDtoCmd;
    private final AccountGroupUpdateInLeaveCmd accountGroupUpdateInLeaveCmd;
    private final ActivityCreateCmd activityCreateCmd;
    private final ActivityTypeService activityTypeService;
    private final GroupMembershipCreateCmd groupMembershipCreateCmd;
    private final RequestStateGetIdByCodeCmd requestStateGetIdByCodeCmd;

    @Override
    protected Void run(Request request) {
        AccountGroupDto accountGroupDto = accountGroupGetDtoCmd.withRequest(
                AccountGroupGetDtoCmd.Request.builder()
                    .accountGroupId(request.getAccountGroupId())
                    .build())
            .execute();

        if (accountGroupDto.getGroupState()
            .getCode()
            .equals("LEFT"))
        {
            throw new AccountGroupException(
                "already.left", "'" + accountGroupDto.getGroup()
                .getName() + "'", HttpStatus.CONFLICT.value()
            );
        }

        AccountGroupLeaveRequest leaveRequest = request.getLeaveRequest();

        accountGroupUpdateInLeaveCmd.withRequest(AccountGroupUpdateInLeaveCmd.Request.builder()
                .action(GroupActionEnum.LEAVE)
                .leftAt(leaveRequest.getLeaveDate())
                .accountId(accountGroupDto.getAccountId())
                .groupId(accountGroupDto.getGroupId())
                .build())
            .execute();

        ActivityType activityType = activityTypeService.findByCodeThrow(ActivityTypeCodeEnum.GRUPO);
        UUID requestStateId = requestStateGetIdByCodeCmd.withRequest(
                RequestStateGetIdByCodeCmd.Request.builder()
                    .code(RequestStateCodeEnum.NOTHING_WAS_REQUESTED)
                    .build())
            .execute();

        UUID activityId = activityCreateCmd.withRequest(ActivityCreateCmd.Request.builder()
                .link("N/A")
                .activityDate(leaveRequest.getLeaveDate())
                .accountId(accountGroupDto.getAccountId())
                .activityTypeId(activityType.getId())
                .build())
            .execute();

        groupMembershipCreateCmd.withRequest(GroupMembershipCreateCmd.Request.builder()
                .action(GroupActionEnum.LEAVE)
                .groupId(accountGroupDto.getGroupId())
                .requestStateId(requestStateId)
                .activityId(activityId)
                .activityTypeId(activityType.getId())
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID accountGroupId;
        @NotNull
        private final AccountGroupLeaveRequest leaveRequest;
    }
}
