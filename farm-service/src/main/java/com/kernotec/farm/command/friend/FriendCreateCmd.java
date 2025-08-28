package com.kernotec.farm.command.friend;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.jpa.entity.Friend;
import com.kernotec.farm.jpa.enums.FriendStatusEnum;
import com.kernotec.farm.jpa.enums.FriendTypeEnum;
import com.kernotec.farm.jpa.service.FriendService;
import jakarta.validation.constraints.NotNull;
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

        friend.setName(request.getName());
        friend.setStatus(request.getStatus());
        friend.setType(request.getType());
        friend.setActivityId(request.getActivityId());
        friend.setActivityTypeId(request.getActivityTypeId());

        friend = friendService.save(friend);
        return friend.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String name;
        @NotNull
        private final FriendStatusEnum status;
        @NotNull
        private final FriendTypeEnum type;
        @NotNull
        private final UUID activityId;
        @NotNull
        private final UUID activityTypeId;
    }
}
