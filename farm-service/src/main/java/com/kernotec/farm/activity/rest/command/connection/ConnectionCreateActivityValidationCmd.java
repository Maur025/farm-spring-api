package com.kernotec.farm.activity.rest.command.connection;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.Friend;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.account.jpa.service.FriendService;
import com.kernotec.farm.activity.exception.ActivityException;
import com.kernotec.farm.activity.exception.FriendException;
import com.kernotec.farm.activity.jpa.enums.ConnectionActionEnum;
import com.kernotec.farm.parametric.jpa.entity.FriendState;
import com.kernotec.farm.parametric.jpa.enums.FriendStateCodeEnum;
import com.kernotec.farm.parametric.jpa.service.FriendStateService;
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
public class ConnectionCreateActivityValidationCmd extends
    AbstractTransactionalRequiredCommand<ConnectionCreateActivityValidationCmd.Request, Void>
{

    private final FriendService friendService;
    private final FriendStateService friendStateService;

    @Override
    protected Void run(Request request) {
        FriendState friendState = friendStateService.findByCodeThrow(FriendStateCodeEnum.ACTIVE);

        Optional<Friend> friend = friendService.findByAccountIdAndFriendAccountIdAndFriendStateId(
            request.getAccountId(), request.getPotentialFriendAccountId(), friendState.getId());

        if (friend.isPresent()) {
            throw new FriendException(
                "friendship.already.exists",
                "accountId: '" + request.getAccountUsername() + "', friendAccountId: '"
                    + request.getPotentialFriendAccountUsername() + "'",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        if (ConnectionActionEnum.INCOMING_FRIEND_REQUEST.equals(request.getAction())
            && AccountTypeEnum.INTERNAL.equals(request.getAccountType()))
        {
            throw new ActivityException(
                "action.not.supported",
                String.format(
                    "action -> %s in type -> %s", request.getAction(), request.getAccountType()),
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
        private final String accountUsername;
        @NotNull
        private final UUID potentialFriendAccountId;
        @NotNull
        private final String potentialFriendAccountUsername;

        @NotNull
        private final AccountTypeEnum accountType;
        @NotNull
        private final ConnectionActionEnum action;
    }
}
