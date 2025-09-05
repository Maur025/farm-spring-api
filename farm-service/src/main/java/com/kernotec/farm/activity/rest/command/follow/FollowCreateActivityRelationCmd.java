package com.kernotec.farm.activity.rest.command.follow;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.command.follow.FollowCreateCmd;
import com.kernotec.farm.activity.rest.dto.request.follow.FollowCreateRequest;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FollowCreateActivityRelationCmd extends
    AbstractTransactionalRequiredCommand<FollowCreateActivityRelationCmd.Request, Void>
{

    private final FollowCreateCmd followCreateCmd;

    @Override
    protected Void run(Request request) {
        FollowCreateRequest followRequest = request.getFollowRequest();

        if (followRequest == null || followRequest.getName() == null || followRequest.getName()
            .isBlank())
        {
            log.debug("FollowCreateRequest is null or name is blank");

            return null;
        }

        followCreateCmd.withRequest(FollowCreateCmd.Request.builder()
                .name(followRequest.getName())
                .isFollowing(followRequest.getIsFollowing() != null && followRequest.getIsFollowing())
                .activityId(request.getActivityId())
                .activityTypeId(request.getActivityTypeId())
                .publishingContextId(followRequest.getPublishingContextId())
                .regionId(followRequest.getRegionId())
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        private final FollowCreateRequest followRequest;
        private final UUID activityId;
        private final UUID activityTypeId;
    }
}
