package com.kernotec.farm.account.command.friend;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.Friend;
import com.kernotec.farm.account.jpa.service.FriendService;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FriendCreateCmd extends
    AbstractTransactionalRequiredCommand<FriendCreateCmd.Request, UUID>
{

    private final FriendService friendService;

    @Override
    protected UUID run(Request request) {
        Friend friend = new Friend();

        friend.setAccountId(request.getAccountId());
        friend.setFriendAccountId(request.getFriendAccountId());

        friend = friendService.save(friend);
        return friend.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final ZonedDateTime acceptedAt;
        private final ZonedDateTime endedAt;
        @NotNull
        private final UUID accountId;
        @NotNull
        private final UUID friendAccountId;
        @NotNull
        private final UUID friendStateId;
    }
}
