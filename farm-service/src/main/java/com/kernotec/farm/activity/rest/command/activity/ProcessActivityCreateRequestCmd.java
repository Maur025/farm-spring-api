package com.kernotec.farm.activity.rest.command.activity;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.command.activity.ActivityCreateCmd;
import com.kernotec.farm.activity.exception.ActivityException;
import com.kernotec.farm.activity.rest.command.comment.CommentCreateActivityRelationCmd;
import com.kernotec.farm.activity.rest.command.connection.ConnectionCreateActivityRelationCmd;
import com.kernotec.farm.activity.rest.command.follow.FollowCreateActivityRelationCmd;
import com.kernotec.farm.activity.rest.command.group.membership.GroupMembershipCreateActivityRelationCmd;
import com.kernotec.farm.activity.rest.command.publishing.PublishingCreateActivityRelationCmd;
import com.kernotec.farm.activity.rest.command.reaction.ReactionCreateActivityRelationCmd;
import com.kernotec.farm.activity.rest.dto.request.activity.ActivityCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.comment.CommentCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.follow.FollowCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.publishing.PublishingCreateRequest;
import com.kernotec.farm.parametric.command.activity.type.ActivityTypeGetDtoCmd;
import com.kernotec.farm.parametric.jpa.dto.entity.ActivityTypeDto;
import com.kernotec.farm.parametric.jpa.enums.ActivityTypeCodeEnum;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
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
    private final ActivityTypeGetDtoCmd activityTypeGetDtoCmd;

    @Override
    protected void validate(Request request) {
        ActivityCreateRequest activityRequest = request.getActivityRequest();

        ActivityTypeDto activityTypeDto = activityTypeGetDtoCmd.withRequest(
                ActivityTypeGetDtoCmd.Request.builder()
                    .activityTypeId(activityRequest.getActivityTypeId())
                    .build())
            .execute();

        switch (ActivityTypeCodeEnum.fromValue(activityTypeDto.getCode())) {
            case GRUPO -> {
                if (activityRequest.getGroupMembership() == null ||
                    activityRequest.getGroupMembership()
                        .getAction() == null)
                {
                    launchExceptionDataMissing("groupMembership");
                }
            }
            case AMISTAD -> {
                if (activityRequest.getConnection() == null || activityRequest.getConnection()
                    .getAction() == null)
                {
                    launchExceptionDataMissing("connection");
                }
            }
            case FOLLOW -> {
                if (activityRequest.getFollow() == null) {
                    launchExceptionDataMissing("follow");
                }

                FollowCreateRequest follow = activityRequest.getFollow();

                if (follow.getName() == null || follow.getName()
                    .isBlank() || follow.getIsFollowing() == null
                    || follow.getPublishingContextId() == null || follow.getRegionId() == null)
                {
                    launchExceptionDataMissing("follow");
                }
            }
            case REACCION -> {
                if (activityRequest.getReaction() == null || activityRequest.getReaction()
                    .getReactionTypeId() == null)
                {
                    launchExceptionDataMissing("reaction");
                }
            }
            case COMENTARIO -> {
                if (activityRequest.getComment() == null) {
                    launchExceptionDataMissing("comment");
                }

                CommentCreateRequest commentRequest = activityRequest.getComment();

                if (commentRequest.getComment() == null || commentRequest.getComment()
                    .isBlank() || commentRequest.getIsAgreeComment() == null
                    || commentRequest.getPublishingContextId() == null)
                {
                    launchExceptionDataMissing("comment");
                }
            }
            case PUBLICACION -> {
                if (activityRequest.getPublishing() == null) {
                    launchExceptionDataMissing("publishing");
                }

                PublishingCreateRequest publishing = activityRequest.getPublishing();

                if (publishing.getPublishingTypeId() == null
                    || publishing.getPublishingContextId() == null)
                {
                    launchExceptionDataMissing("publishing");
                }
            }
        }
    }

    private void launchExceptionDataMissing(String fieldName) {
        throw new ActivityException(
            "data.missing", "'" + fieldName + "'", HttpStatus.BAD_REQUEST.value());
    }

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
                    .activityDate(activityRequest.getActivityDate())
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
