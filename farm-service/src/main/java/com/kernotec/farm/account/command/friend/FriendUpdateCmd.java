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
public class FriendUpdateCmd extends
    AbstractTransactionalRequiredCommand<FriendUpdateCmd.Request, UUID>
{

    private final FriendService friendService;

    @Override
    protected UUID run(Request request) {
        Friend friend = friendService.findByIdThrow(request.getFriendId());

        if (request.getEndedAt() != null) {
            friend.setEndedAt(request.getEndedAt());
        }

        if (request.getFriendStateId() != null) {
            friend.setFriendStateId(request.getFriendStateId());
        }

        friend = friendService.save(friend);
        return friend.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID friendId;

        private final ZonedDateTime endedAt;
        private final UUID friendStateId;
    }
}
