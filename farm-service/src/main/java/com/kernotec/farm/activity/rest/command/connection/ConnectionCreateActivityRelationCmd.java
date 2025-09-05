package com.kernotec.farm.activity.rest.command.connection;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.account.AccountCreateCmd;
import com.kernotec.farm.account.command.account.AccountGetDtoCmd;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.activity.command.connection.ConnectionCreateCmd;
import com.kernotec.farm.activity.jpa.enums.ConnectionTypeEnum;
import com.kernotec.farm.activity.rest.dto.request.connection.ConnectionCreateRequest;
import com.kernotec.farm.parametric.command.request.state.RequestStateGetIdByCodeCmd;
import com.kernotec.farm.parametric.jpa.enums.RequestStateCodeEnum;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConnectionCreateActivityRelationCmd extends
    AbstractTransactionalRequiredCommand<ConnectionCreateActivityRelationCmd.Request, Void>
{

    private final RequestStateGetIdByCodeCmd requestStateGetIdByCodeCmd;
    private final AccountGetDtoCmd accountGetDtoCmd;
    private final AccountCreateCmd accountCreateCmd;
    private final ConnectionCreateCmd connectionCreateCmd;
    private final ConnectionCreateActivityValidationCmd connectionCreateActivityValidationCmd;

    @Override
    protected Void run(Request request) {
        ConnectionCreateRequest connectionRequest = request.getConnectionRequest();

        if (connectionRequest == null) {
            log.debug("No connection request provided, skipping activity relation creation.");
            return null;
        }

        UUID requestStateId = requestStateGetIdByCodeCmd.withRequest(
                RequestStateGetIdByCodeCmd.Request.builder()
                    .code(RequestStateCodeEnum.PENDING)
                    .build())
            .execute();

        AccountDto accountDto = accountGetDtoCmd.withRequest(AccountGetDtoCmd.Request.builder()
                .accountId(request.getAccountId())
                .build())
            .execute();

        UUID potentialAccountId = connectionRequest.getPotentialFriendAccountId();

        if (connectionRequest.getIsNewAccount() && potentialAccountId == null) {
            potentialAccountId = accountCreateCmd.withRequest(AccountCreateCmd.Request.builder()
                    .username(connectionRequest.getFriendUsername())
                    .password("N/A")
                    .socialNetworkId(accountDto.getSocialNetworkId())
                    .type(AccountTypeEnum.EXTERNAL)
                    .build())
                .execute();
        }

        AccountDto accountPotentialDto = accountGetDtoCmd.withRequest(
                AccountGetDtoCmd.Request.builder()
                    .accountId(potentialAccountId)
                    .build())
            .execute();

        connectionCreateActivityValidationCmd.withRequest(
                ConnectionCreateActivityValidationCmd.Request.builder()
                    .accountId(request.getAccountId())
                    .accountUsername(accountDto.getUsername())
                    .potentialFriendAccountId(potentialAccountId)
                    .potentialFriendAccountUsername(accountPotentialDto.getUsername())
                    .build())
            .execute();

        connectionCreateCmd.withRequest(ConnectionCreateCmd.Request.builder()
                .potentialFriendAccountId(potentialAccountId)
                .action(connectionRequest.getAction())
                .type(ConnectionTypeEnum.fromValue(accountPotentialDto.getType()))
                .requestStateId(requestStateId)
                .activityId(request.getActivityId())
                .activityTypeId(request.getActivityTypeId())
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        private final ConnectionCreateRequest connectionRequest;
        private final UUID activityId;
        private final UUID activityTypeId;
        private final UUID accountId;
    }
}
