package com.kernotec.farm.command.follow;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.jpa.entity.Follow;
import com.kernotec.farm.jpa.service.FollowService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FollowCreateCmd extends
    AbstractTransactionalRequiredCommand<FollowCreateCmd.Request, UUID>
{

    private final FollowService followService;

    @Override
    protected UUID run(Request request) {
        Follow follow = new Follow();

        follow.setName(request.getName());
        follow.setFollowing(request.isFollowing());
        follow.setActivityId(request.getActivityId());
        follow.setActivityTypeId(request.getActivityTypeId());

        follow = followService.save(follow);
        return follow.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String name;
        @NotNull
        private final boolean isFollowing;
        @NotNull
        private final UUID activityId;
        @NotNull
        private final UUID activityTypeId;
    }
}
