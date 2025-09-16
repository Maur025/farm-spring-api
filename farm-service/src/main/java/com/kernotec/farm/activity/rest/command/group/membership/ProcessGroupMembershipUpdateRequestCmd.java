package com.kernotec.farm.activity.rest.command.group.membership;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.account.group.AccountGroupCreateCmd;
import com.kernotec.farm.activity.command.group.membership.GroupMembershipGetDtoCmd;
import com.kernotec.farm.activity.command.group.membership.GroupMembershipUpdateCmd;
import com.kernotec.farm.activity.jpa.dto.entity.GroupMembershipDto;
import com.kernotec.farm.activity.jpa.enums.ResponseRequestStateEnum;
import com.kernotec.farm.activity.rest.dto.request.group.membership.GroupMembershipUpdateRequest;
import com.kernotec.farm.parametric.jpa.entity.GroupState;
import com.kernotec.farm.parametric.jpa.entity.RequestState;
import com.kernotec.farm.parametric.jpa.enums.GroupStateCodeEnum;
import com.kernotec.farm.parametric.jpa.enums.RequestStateCodeEnum;
import com.kernotec.farm.parametric.jpa.service.GroupStateService;
import com.kernotec.farm.parametric.jpa.service.RequestStateService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessGroupMembershipUpdateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessGroupMembershipUpdateRequestCmd.Request, Void>
{

    private final RequestStateService requestStateService;
    private final GroupMembershipUpdateCmd groupMembershipUpdateCmd;
    private final AccountGroupCreateCmd accountGroupCreateCmd;
    private final GroupMembershipGetDtoCmd groupMembershipGetDtoCmd;
    private final GroupStateService groupStateService;

    @Override
    protected Void run(Request request) {
        GroupMembershipUpdateRequest groupMembershipRequest = request.getGroupMembershipRequest();

        RequestState requestState = requestStateService.findByCodeThrow(
            RequestStateCodeEnum.fromValue(groupMembershipRequest.getResponseRequestState()));

        if (groupMembershipRequest.getResponseRequestState()
            .equals(ResponseRequestStateEnum.APPROVED))
        {
            GroupMembershipDto groupMembershipDto = groupMembershipGetDtoCmd.withRequest(
                    GroupMembershipGetDtoCmd.Request.builder()
                        .groupMembershipId(request.getGroupMembershipId())
                        .build())
                .execute();

            GroupState groupState = groupStateService.findByCodeThrow(GroupStateCodeEnum.JOINED);

            accountGroupCreateCmd.withRequest(AccountGroupCreateCmd.Request.builder()
                    .joinedAt(groupMembershipRequest.getResponseDate())
                    .accountId(groupMembershipDto.getActivity()
                        .getAccountId())
                    .groupId(groupMembershipDto.getGroupId())
                    .groupStateId(groupState.getId())
                    .build())
                .execute();
        }

        groupMembershipUpdateCmd.withRequest(GroupMembershipUpdateCmd.Request.builder()
                .groupMembershipId(request.getGroupMembershipId())
                .requestStateId(requestState.getId())
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID groupMembershipId;

        @NotNull
        private final GroupMembershipUpdateRequest groupMembershipRequest;
    }
}
