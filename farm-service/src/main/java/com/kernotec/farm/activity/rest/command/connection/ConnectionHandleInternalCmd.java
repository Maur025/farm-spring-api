package com.kernotec.farm.activity.rest.command.connection;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.activity.command.activity.ActivityCreateCmd;
import com.kernotec.farm.activity.command.connection.ConnectionCreateCmd;
import com.kernotec.farm.activity.jpa.enums.ConnectionActionEnum;
import com.kernotec.farm.activity.jpa.enums.ConnectionTypeEnum;
import com.kernotec.farm.activity.jpa.service.ConnectionHandleService;
import com.kernotec.farm.parametric.command.request.state.RequestStateGetIdByCodeCmd;
import com.kernotec.farm.parametric.jpa.enums.RequestStateCodeEnum;
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
public class ConnectionHandleInternalCmd extends
    AbstractTransactionalRequiredCommand<ConnectionHandleInternalCmd.Request, Void>
{

    private final ConnectionCreateCmd connectionCreateCmd;
    private final RequestStateGetIdByCodeCmd requestStateGetIdByCodeCmd;
    private final ConnectionHandleService connectionHandleService;
    private final ActivityCreateCmd activityCreateCmd;

    @Override
    protected Void run(Request request) {
        if (!isInternalHandleAction(request.getTargetAccountType(), request.getAction())) {
            log.debug("Not an account internal handle action, skipping.");
            return null;
        }

        AccountDto targetAccountDto = request.getTargetAccountDto();
        AccountDto sourceAccountDto = request.getSourceAccountDto();

        UUID targetActivityId = activityCreateCmd.withRequest(ActivityCreateCmd.Request.builder()
                .link("N/A")
                .activityDate(request.getActivityDate())
                .accountId(targetAccountDto.getId())
                .activityTypeId(request.getSourceActivityTypeId())
                .isSystemActivity(
                    ConnectionActionEnum.OUTGOING_FRIEND_REQUEST.equals(request.getAction()))
                .build())
            .execute();

        /* Target Connection Register */
        connectionCreateCmd.withRequest(ConnectionCreateCmd.Request.builder()
                .potentialFriendAccountId(sourceAccountDto.getId())
                .action(ConnectionActionEnum.OUTGOING_FRIEND_REQUEST.equals(request.getAction())
                    ? ConnectionActionEnum.INCOMING_FRIEND_REQUEST
                    : ConnectionActionEnum.OUTGOING_FRIEND_REQUEST)
                .type(ConnectionTypeEnum.INTERNAL)
                .requestStateId(
                    connectionHandleService.getReqStatePendingOrApprovedIdByAction(request.getAction()))
                .activityId(targetActivityId)
                .activityTypeId(request.getSourceActivityTypeId())
                .build())
            .execute();

        /* Source Connection Register */
        connectionCreateCmd.withRequest(ConnectionCreateCmd.Request.builder()
                .potentialFriendAccountId(targetAccountDto.getId())
                .action(request.getAction())
                .type(ConnectionTypeEnum.INTERNAL)
                .requestStateId(ConnectionActionEnum.OUTGOING_FRIEND_REQUEST.equals(request.getAction())
                    ? getReqStateNothingWasRequestedId()
                    : connectionHandleService.getReqStatePendingOrApprovedIdByAction(
                        request.getAction()))
                .activityId(request.getSourceActivityId())
                .activityTypeId(request.getSourceActivityTypeId())
                .build())
            .execute();

        return null;
    }

    public boolean isInternalHandleAction(AccountTypeEnum targetAccountType,
        ConnectionActionEnum action)
    {
        return AccountTypeEnum.INTERNAL.equals(targetAccountType) && (
            ConnectionActionEnum.OUTGOING_FRIEND_REQUEST.equals(action)
                || ConnectionActionEnum.INCOMING_FRIEND_REQUEST_AND_CONFIRMED.equals(action));
    }

    private UUID getReqStateNothingWasRequestedId() {
        return requestStateGetIdByCodeCmd.withRequest(RequestStateGetIdByCodeCmd.Request.builder()
                .code(RequestStateCodeEnum.NOTHING_WAS_REQUESTED)
                .build())
            .execute();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final AccountTypeEnum targetAccountType;
        @NotNull
        private final ConnectionActionEnum action;
        @NotNull
        private final AccountDto sourceAccountDto;
        @NotNull
        private final AccountDto targetAccountDto;
        @NotNull
        private final UUID sourceActivityId;
        @NotNull
        private final UUID sourceActivityTypeId;
        @NotNull
        private final ZonedDateTime activityDate;
    }
}
