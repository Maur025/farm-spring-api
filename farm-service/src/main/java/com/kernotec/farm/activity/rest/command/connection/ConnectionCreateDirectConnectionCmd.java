package com.kernotec.farm.activity.rest.command.connection;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.friend.FriendCreateCmd;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.activity.jpa.enums.ConnectionActionEnum;
import com.kernotec.farm.parametric.command.friend.state.FriendStateGetIdByCodeCmd;
import com.kernotec.farm.parametric.jpa.enums.FriendStateCodeEnum;
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
public class ConnectionCreateDirectConnectionCmd extends
    AbstractTransactionalRequiredCommand<ConnectionCreateDirectConnectionCmd.Request, Void>
{

    private final FriendCreateCmd friendCreateCmd;
    private final FriendStateGetIdByCodeCmd friendStateGetIdByCodeCmd;

    @Override
    protected Void run(Request request) {
        if (!request.getAction()
            .equals(ConnectionActionEnum.INCOMING_FRIEND_REQUEST_AND_CONFIRMED))
        {
            log.debug("Action is not INCOMING_FRIEND_REQUEST_AND_CONFIRMED, do nothing");
            return null;
        }

        UUID friendStateId = friendStateGetIdByCodeCmd.withRequest(
                FriendStateGetIdByCodeCmd.Request.builder()
                    .code(FriendStateCodeEnum.ACTIVE)
                    .build())
            .execute();

        AccountDto accountDto = request.getAccountDto();
        AccountDto accountFriendDto = request.getAccountFriendDto();

        friendCreateCmd.withRequest(FriendCreateCmd.Request.builder()
                .acceptedAt(request.getActivityDate())
                .accountId(accountDto.getId())
                .friendAccountId(accountFriendDto.getId())
                .friendStateId(friendStateId)
                .build())
            .execute();

        friendCreateCmd.withRequest(FriendCreateCmd.Request.builder()
                .acceptedAt(request.getActivityDate())
                .accountId(accountFriendDto.getId())
                .friendAccountId(accountDto.getId())
                .friendStateId(friendStateId)
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final AccountDto accountDto;
        @NotNull
        private final AccountDto accountFriendDto;
        @NotNull
        private final ConnectionActionEnum action;
        @NotNull
        private final ZonedDateTime activityDate;
    }
}
