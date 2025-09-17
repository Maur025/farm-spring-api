package com.kernotec.farm.activity.rest.command.connection;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.activity.command.connection.ConnectionCreateCmd;
import com.kernotec.farm.activity.jpa.enums.ConnectionActionEnum;
import com.kernotec.farm.activity.jpa.enums.ConnectionTypeEnum;
import com.kernotec.farm.activity.jpa.service.ConnectionHandleService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConnectionHandleExternalCmd extends
    AbstractTransactionalRequiredCommand<ConnectionHandleExternalCmd.Request, Void>
{

    private final ConnectionCreateCmd connectionCreateCmd;
    private final ConnectionHandleService connectionHandleService;

    @Override
    protected Void run(Request request) {
        if (!isExternalHandleAction(request.getTargetAccountType(), request.getAction())) {
            log.debug("Not an account external handle action, skipping.");
            return null;
        }

        AccountDto targetAccountDto = request.getTargetAccountDto();

        connectionCreateCmd.withRequest(ConnectionCreateCmd.Request.builder()
                .potentialFriendAccountId(targetAccountDto.getId())
                .action(request.getAction())
                .type(ConnectionTypeEnum.EXTERNAL)
                .requestStateId(
                    connectionHandleService.getReqStatePendingOrApprovedIdByAction(request.getAction()))
                .activityId(request.getActivityId())
                .activityTypeId(request.getActivityTypeId())
                .build())
            .execute();

        return null;
    }

    public boolean isExternalHandleAction(AccountTypeEnum targetAccountType,
        ConnectionActionEnum action)
    {
        return AccountTypeEnum.EXTERNAL.equals(targetAccountType) && (
            ConnectionActionEnum.OUTGOING_FRIEND_REQUEST.equals(action)
                || ConnectionActionEnum.INCOMING_FRIEND_REQUEST_AND_CONFIRMED.equals(action)
                || ConnectionActionEnum.INCOMING_FRIEND_REQUEST.equals(action));
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final AccountTypeEnum targetAccountType;
        @NotNull
        private final ConnectionActionEnum action;
        @NotNull
        private final AccountDto targetAccountDto;
        @NotNull
        private final UUID activityId;
        @NotNull
        private final UUID activityTypeId;
    }
}
