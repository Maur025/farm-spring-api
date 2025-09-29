package com.kernotec.farm.activity.rest.command.connection;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.account.AccountCreateCmd;
import com.kernotec.farm.account.command.account.AccountGetDtoCmd;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.account.jpa.service.AccountService;
import com.kernotec.farm.activity.rest.dto.request.connection.ConnectionCreateRequest;
import com.kernotec.farm.util.LinkUtil;
import java.time.ZonedDateTime;
import java.util.Optional;
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

    private final AccountGetDtoCmd accountGetDtoCmd;
    private final AccountCreateCmd accountCreateCmd;
    private final ConnectionCreateActivityValidationCmd connectionCreateActivityValidationCmd;
    private final ConnectionCreateDirectConnectionCmd connectionCreateDirectConnectionCmd;
    private final ConnectionHandleExternalCmd connectionHandleExternalCmd;
    private final ConnectionHandleInternalCmd connectionHandleInternalCmd;
    private final AccountService accountService;
    private final LinkUtil linkUtil;

    @Override
    protected Void run(Request request) {
        ConnectionCreateRequest connectionRequest = request.getConnectionRequest();

        if (connectionRequest == null) {
            log.debug("No connection request provided, skipping activity relation creation.");
            return null;
        }

        AccountDto sourceAccountDto = accountGetDtoCmd.withRequest(
                AccountGetDtoCmd.Request.builder()
                    .accountId(request.getAccountId())
                    .build())
            .execute();

        Optional<Account> accountOptionalByLink = accountService.findByAccountLinkAndSocialNetworkId(
            request.getAccountLink(), sourceAccountDto.getSocialNetworkId());

        UUID targetAccountId = accountOptionalByLink.map(Account::getId)
            .orElseGet(() -> {
                if (connectionRequest.getPotentialFriendAccountId() != null) {
                    throw new IllegalArgumentException(
                        "The provided potentialFriendAccountId does not exist in the system.");
                }

                return accountCreateCmd.withRequest(AccountCreateCmd.Request.builder()
                        .username(connectionRequest.getFriendUsername())
                        .password("N/A")
                        .socialNetworkId(sourceAccountDto.getSocialNetworkId())
                        .type(AccountTypeEnum.EXTERNAL)
                        .accountLink(request.getAccountLink())
                        .identityUsername(linkUtil.getIdentityFacebookOfLink(request.getAccountLink()))
                        .build())
                    .execute();
            });

        AccountDto targetAccountDto = accountGetDtoCmd.withRequest(
                AccountGetDtoCmd.Request.builder()
                    .accountId(targetAccountId)
                    .build())
            .execute();

        connectionCreateActivityValidationCmd.withRequest(
                ConnectionCreateActivityValidationCmd.Request.builder()
                    .accountId(request.getAccountId())
                    .accountUsername(sourceAccountDto.getUsername())
                    .potentialFriendAccountId(targetAccountId)
                    .potentialFriendAccountUsername(targetAccountDto.getUsername())
                    .accountType(targetAccountDto.getType())
                    .action(connectionRequest.getAction())
                    .build())
            .execute();

        connectionHandleExternalCmd.withRequest(ConnectionHandleExternalCmd.Request.builder()
                .targetAccountType(targetAccountDto.getType())
                .action(connectionRequest.getAction())
                .targetAccountDto(targetAccountDto)
                .activityId(request.getActivityId())
                .activityTypeId(request.getActivityTypeId())
                .build())
            .execute();

        connectionHandleInternalCmd.withRequest(ConnectionHandleInternalCmd.Request.builder()
                .targetAccountType(targetAccountDto.getType())
                .action(connectionRequest.getAction())
                .targetAccountDto(targetAccountDto)
                .sourceAccountDto(sourceAccountDto)
                .sourceActivityId(request.getActivityId())
                .sourceActivityTypeId(request.getActivityTypeId())
                .activityDate(request.getActivityDate())
                .build())
            .execute();

        connectionCreateDirectConnectionCmd.withRequest(
                ConnectionCreateDirectConnectionCmd.Request.builder()
                    .accountDto(sourceAccountDto)
                    .accountFriendDto(targetAccountDto)
                    .action(connectionRequest.getAction())
                    .activityDate(request.getActivityDate())
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
        private final ZonedDateTime activityDate;
        private final String accountLink;
    }
}
