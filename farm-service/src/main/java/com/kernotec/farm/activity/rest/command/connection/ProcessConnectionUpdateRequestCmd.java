package com.kernotec.farm.activity.rest.command.connection;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.account.AccountGetDtoCmd;
import com.kernotec.farm.account.command.friend.FriendCreateCmd;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.activity.command.activity.ActivityCreateCmd;
import com.kernotec.farm.activity.command.connection.ConnectionCreateCmd;
import com.kernotec.farm.activity.command.connection.ConnectionGetDtoCmd;
import com.kernotec.farm.activity.command.connection.ConnectionUpdateCmd;
import com.kernotec.farm.activity.jpa.dto.entity.ConnectionDto;
import com.kernotec.farm.activity.jpa.entity.Connection;
import com.kernotec.farm.activity.jpa.enums.ConnectionActionEnum;
import com.kernotec.farm.activity.jpa.enums.ConnectionTypeEnum;
import com.kernotec.farm.activity.jpa.enums.ResponseRequestStateEnum;
import com.kernotec.farm.activity.jpa.service.ConnectionService;
import com.kernotec.farm.activity.rest.dto.request.connection.ConnectionUpdateRequest;
import com.kernotec.farm.parametric.command.friend.state.FriendStateGetIdByCodeCmd;
import com.kernotec.farm.parametric.command.request.state.RequestStateGetIdByCodeCmd;
import com.kernotec.farm.parametric.jpa.enums.FriendStateCodeEnum;
import com.kernotec.farm.parametric.jpa.enums.RequestStateCodeEnum;
import jakarta.validation.constraints.NotNull;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessConnectionUpdateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessConnectionUpdateRequestCmd.Request, Void>
{

    private final ConnectionUpdateCmd connectionUpdateCmd;
    private final ConnectionGetDtoCmd connectionGetDtoCmd;
    private final FriendCreateCmd friendCreateCmd;
    private final FriendStateGetIdByCodeCmd friendStateGetIdByCodeCmd;
    private final ActivityCreateCmd activityCreateCmd;
    private final RequestStateGetIdByCodeCmd requestStateGetIdByCodeCmd;
    private final ConnectionCreateCmd connectionCreateCmd;
    private final AccountGetDtoCmd accountGetDtoCmd;
    private final ConnectionService connectionService;

    @Override
    protected Void run(Request request) {
        ConnectionUpdateRequest connectionRequest = request.getConnectionRequest();
        connectionRequest.setResponseDate(ZonedDateTime.now(ZoneOffset.UTC));

        UUID approveOrRejectRequestStateId = requestStateGetIdByCodeCmd.withRequest(
                RequestStateGetIdByCodeCmd.Request.builder()
                    .code(RequestStateCodeEnum.fromValue(connectionRequest.getResponseRequestState()))
                    .build())
            .execute();

        ConnectionDto connectionDto = connectionGetDtoCmd.withRequest(
                ConnectionGetDtoCmd.Request.builder()
                    .connectionId(request.getConnectionId())
                    .build())
            .execute();

        if (connectionRequest.getResponseRequestState()
            .equals(ResponseRequestStateEnum.APPROVED))
        {
            UUID friendStateId = friendStateGetIdByCodeCmd.withRequest(
                    FriendStateGetIdByCodeCmd.Request.builder()
                        .code(FriendStateCodeEnum.ACTIVE)
                        .build())
                .execute();

            friendCreateCmd.withRequest(FriendCreateCmd.Request.builder()
                    .acceptedAt(connectionRequest.getResponseDate())
                    .accountId(connectionDto.getActivity()
                        .getAccountId())
                    .friendAccountId(connectionDto.getPotentialFriendAccountId())
                    .friendStateId(friendStateId)
                    .build())
                .execute();

            friendCreateCmd.withRequest(FriendCreateCmd.Request.builder()
                    .acceptedAt(connectionRequest.getResponseDate())
                    .accountId(connectionDto.getPotentialFriendAccountId())
                    .friendAccountId(connectionDto.getActivity()
                        .getAccountId())
                    .friendStateId(friendStateId)
                    .build())
                .execute();
        }

        UUID nothingRequestStateId = requestStateGetIdByCodeCmd.withRequest(
                RequestStateGetIdByCodeCmd.Request.builder()
                    .code(RequestStateCodeEnum.NOTHING_WAS_REQUESTED)
                    .build())
            .execute();

        UUID responseActivityId = activityCreateCmd.withRequest(ActivityCreateCmd.Request.builder()
                .link("N/A")
                .activityDate(connectionRequest.getResponseDate())
                .accountId(connectionDto.getActivity()
                    .getAccountId())
                .activityTypeId(connectionDto.getActivityTypeId())
                .build())
            .execute();

        AccountDto accountDto = accountGetDtoCmd.withRequest(AccountGetDtoCmd.Request.builder()
                .accountId(connectionDto.getPotentialFriendAccountId())
                .build())
            .execute();

        connectionCreateCmd.withRequest(ConnectionCreateCmd.Request.builder()
                .potentialFriendAccountId(connectionDto.getPotentialFriendAccountId())
                .action(ResponseRequestStateEnum.APPROVED.equals(
                    connectionRequest.getResponseRequestState())
                    ? ConnectionActionEnum.APPROVE_CONNECTION : ConnectionActionEnum.REJECT_CONNECTION)
                .type(ConnectionTypeEnum.fromValue(accountDto.getType()))
                .requestStateId(nothingRequestStateId)
                .activityId(responseActivityId)
                .activityTypeId(connectionDto.getActivityTypeId())
                .build())
            .execute();

        if (AccountTypeEnum.INTERNAL.equals(accountDto.getType())) {
            UUID requestStateId = requestStateGetIdByCodeCmd.withRequest(
                    RequestStateGetIdByCodeCmd.Request.builder()
                        .code(RequestStateCodeEnum.NOTHING_WAS_REQUESTED)
                        .build())
                .execute();

            Connection connectionMirror = connectionService.findByPotentialFriendAccountIdAndActionAndRequestStateIdAndTypeThrow(
                connectionDto.getActivity()
                    .getAccountId(), ConnectionActionEnum.OUTGOING_FRIEND_REQUEST, requestStateId,
                ConnectionTypeEnum.INTERNAL
            );

            connectionUpdateCmd.withRequest(ConnectionUpdateCmd.Request.builder()
                    .connectionId(connectionMirror.getId())
                    .requestStateId(approveOrRejectRequestStateId)
                    .build())
                .execute();
        }

        connectionUpdateCmd.withRequest(ConnectionUpdateCmd.Request.builder()
                .connectionId(request.getConnectionId())
                .requestStateId(approveOrRejectRequestStateId)
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID connectionId;

        @NotNull
        private final ConnectionUpdateRequest connectionRequest;
    }
}
