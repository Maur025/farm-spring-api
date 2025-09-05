package com.kernotec.farm.activity.rest.command.group.membership;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.account.AccountGetDtoCmd;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.account.jpa.entity.AccountGroup;
import com.kernotec.farm.account.jpa.service.AccountGroupService;
import com.kernotec.farm.activity.command.group.GroupGetDtoCmd;
import com.kernotec.farm.activity.exception.AccountGroupException;
import com.kernotec.farm.activity.jpa.dto.entity.GroupDto;
import com.kernotec.farm.activity.jpa.enums.GroupActionEnum;
import com.kernotec.farm.parametric.jpa.entity.GroupState;
import com.kernotec.farm.parametric.jpa.enums.GroupStateCodeEnum;
import com.kernotec.farm.parametric.jpa.service.GroupStateService;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupMemberCreateActivityValidationCmd extends
    AbstractTransactionalRequiredCommand<GroupMemberCreateActivityValidationCmd.Request, Void>
{

    private final AccountGroupService accountGroupService;
    private final GroupStateService groupStateService;
    private final AccountGetDtoCmd accountGetDtoCmd;
    private final GroupGetDtoCmd groupGetDtoCmd;

    @Override
    protected Void run(Request request) {
        GroupState groupState = groupStateService.findByCodeThrow(GroupStateCodeEnum.JOINED);

        Optional<AccountGroup> accountGroup = accountGroupService.findByAccountIdAndGroupIdAndGroupStateId(
            request.getAccountId(), request.getGroupId(), groupState.getId());

        AccountDto accountDto = accountGetDtoCmd.withRequest(AccountGetDtoCmd.Request.builder()
                .accountId(request.getAccountId())
                .build())
            .execute();

        GroupDto groupDto = groupGetDtoCmd.withRequest(GroupGetDtoCmd.Request.builder()
                .groupId(request.getGroupId())
                .build())
            .execute();

        if (accountGroup.isEmpty() && request.getAction()
            .equals(GroupActionEnum.LEAVE))
        {
            throw new AccountGroupException(
                "leave.not.is.member",
                "account: '" + accountDto.getUsername() + "',group: '" + groupDto.getName() + "'",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        if (accountGroup.isPresent() && !request.getAction()
            .equals(GroupActionEnum.LEAVE))
        {
            throw new AccountGroupException(
                "join.already.is.member",
                "account: '" + accountDto.getUsername() + "',group: '" + groupDto.getName() + "'",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID accountId;
        @NotNull
        private final UUID groupId;
        @NotNull
        private final GroupActionEnum action;
    }
}
