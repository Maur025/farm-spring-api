package com.kernotec.farm.activity.rest.command.activity;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.command.activity.ActivityCreateCmd;
import com.kernotec.farm.activity.rest.command.comment.CommentCreateActivityRelationCmd;
import com.kernotec.farm.activity.rest.command.connection.ConnectionCreateActivityRelationCmd;
import com.kernotec.farm.activity.rest.command.follow.FollowCreateActivityRelationCmd;
import com.kernotec.farm.activity.rest.command.group.membership.GroupMembershipCreateActivityRelationCmd;
import com.kernotec.farm.activity.rest.command.publishing.PublishingCreateActivityRelationCmd;
import com.kernotec.farm.activity.rest.command.reaction.ReactionCreateActivityRelationCmd;
import com.kernotec.farm.activity.rest.dto.request.activity.ActivityCreateRequest;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessActivityCreateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessActivityCreateRequestCmd.Request, UUID>
{

    private final ActivityCreateCmd activityCreateCmd;
    private final PublishingCreateActivityRelationCmd publishingCreateActivityRelationCmd;
    private final ReactionCreateActivityRelationCmd reactionCreateActivityRelationCmd;
    private final CommentCreateActivityRelationCmd commentCreateActivityRelationCmd;
    private final FollowCreateActivityRelationCmd followCreateActivityRelationCmd;
    private final GroupMembershipCreateActivityRelationCmd groupMembershipCreateActivityRelationCmd;
    private final ConnectionCreateActivityRelationCmd connectionCreateActivityRelationCmd;

    @Override
    protected UUID run(Request request) {
        ActivityCreateRequest activityRequest = request.getActivityRequest();

        UUID activityId = activityCreateCmd.withRequest(ActivityCreateCmd.Request.builder()
                .link(activityRequest.getLink())
                .activityDate(activityRequest.getActivityDate())
                .accountId(activityRequest.getAccountId())
                .activityTypeId(activityRequest.getActivityTypeId())
                .build())
            .execute();

        publishingCreateActivityRelationCmd.withRequest(
                PublishingCreateActivityRelationCmd.Request.builder()
                    .publishingRequest(activityRequest.getPublishing())
                    .activityId(activityId)
                    .activityTypeId(activityRequest.getActivityTypeId())
                    .build())
            .execute();

        reactionCreateActivityRelationCmd.withRequest(
                ReactionCreateActivityRelationCmd.Request.builder()
                    .reactionRequest(activityRequest.getReaction())
                    .activityId(activityId)
                    .activityTypeId(activityRequest.getActivityTypeId())
                    .build())
            .execute();

        commentCreateActivityRelationCmd.withRequest(
                CommentCreateActivityRelationCmd.Request.builder()
                    .commentRequest(activityRequest.getComment())
                    .activityId(activityId)
                    .activityTypeId(activityRequest.getActivityTypeId())
                    .build())
            .execute();

        groupMembershipCreateActivityRelationCmd.withRequest(
                GroupMembershipCreateActivityRelationCmd.Request.builder()
                    .groupMembershipRequest(activityRequest.getGroupMembership())
                    .activityId(activityId)
                    .activityTypeId(activityRequest.getActivityTypeId())
                    .accountId(activityRequest.getAccountId())
                    .activityDate(activityRequest.getActivityDate())
                    .build())
            .execute();

        connectionCreateActivityRelationCmd.withRequest(
                ConnectionCreateActivityRelationCmd.Request.builder()
                    .connectionRequest(activityRequest.getConnection())
                    .activityId(activityId)
                    .activityTypeId(activityRequest.getActivityTypeId())
                    .accountId(activityRequest.getAccountId())
                    .build())
            .execute();

        followCreateActivityRelationCmd.withRequest(
                FollowCreateActivityRelationCmd.Request.builder()
                    .followRequest(activityRequest.getFollow())
                    .activityId(activityId)
                    .activityTypeId(activityRequest.getActivityTypeId())
                    .build())
            .execute();

        return activityId;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final ActivityCreateRequest activityRequest;
    }
}
