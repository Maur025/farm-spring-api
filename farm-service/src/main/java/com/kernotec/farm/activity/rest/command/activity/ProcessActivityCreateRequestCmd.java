package com.kernotec.farm.activity.rest.command.activity;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.account.AccountCreateCmd;
import com.kernotec.farm.account.command.account.AccountGetDtoCmd;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.activity.command.activity.ActivityCreateCmd;
import com.kernotec.farm.activity.command.comment.CommentCreateCmd;
import com.kernotec.farm.activity.command.connection.ConnectionCreateCmd;
import com.kernotec.farm.activity.command.follow.FollowCreateCmd;
import com.kernotec.farm.activity.command.publishing.PublishingCreateCmd;
import com.kernotec.farm.activity.command.reaction.ReactionCreateCmd;
import com.kernotec.farm.activity.rest.dto.request.activity.ActivityCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.comment.CommentCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.connection.ConnectionCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.follow.FollowCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.group.membership.GroupMembershipCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.publishing.PublishingCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.reaction.ReactionCreateRequest;
import com.kernotec.farm.parametric.jpa.entity.RequestState;
import com.kernotec.farm.parametric.jpa.enums.RequestStateCodeEnum;
import com.kernotec.farm.parametric.jpa.service.RequestStateService;
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

    private final RequestStateService requestStateService;

    private final ActivityCreateCmd activityCreateCmd;
    private final PublishingCreateCmd publishingCreateCmd;
    private final ReactionCreateCmd reactionCreateCmd;
    private final CommentCreateCmd commentCreateCmd;
    private final FollowCreateCmd followCreateCmd;
    private final ConnectionCreateCmd connectionCreateCmd;
    private final AccountCreateCmd accountCreateCmd;
    private final AccountGetDtoCmd accountGetDtoCmd;

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

        createPublishingRelation(
            activityRequest.getPublishing(), activityId, activityRequest.getActivityTypeId());

        createReactionRelation(
            activityRequest.getReaction(), activityId, activityRequest.getActivityTypeId());

        createCommentRelation(
            activityRequest.getComment(), activityId, activityRequest.getActivityTypeId());

        createGroupMembershipRelation(
            activityRequest.getGroupMembership(), activityId, activityRequest.getActivityTypeId());

        createConnectionRelation(
            activityRequest.getConnection(), activityId, activityRequest.getActivityTypeId(),
            activityRequest.getAccountId()
        );

        createFollowRelation(
            activityRequest.getFollow(), activityId, activityRequest.getActivityTypeId());

        return activityId;
    }

    public void createPublishingRelation(PublishingCreateRequest publishingRequest, UUID activityId,
        UUID activityTypeId)
    {
        if (publishingRequest == null || publishingRequest.getPublishingContextId() == null) {
            return;
        }

        publishingCreateCmd.withRequest(PublishingCreateCmd.Request.builder()
                .description(publishingRequest.getDescription())
                .publishingTypeId(publishingRequest.getPublishingTypeId())
                .publishingContextId(publishingRequest.getPublishingContextId())
                .activityId(activityId)
                .activityTypeId(activityTypeId)
                .build())
            .execute();
    }

    public void createReactionRelation(ReactionCreateRequest reactionRequest, UUID activityId,
        UUID activityTypeId)
    {
        if (reactionRequest == null || reactionRequest.getReactionTypeId() == null) {
            return;
        }

        reactionCreateCmd.withRequest(ReactionCreateCmd.Request.builder()
                .reactionTypeId(reactionRequest.getReactionTypeId())
                .activityId(activityId)
                .activityTypeId(activityTypeId)
                .build())
            .execute();
    }

    public void createCommentRelation(CommentCreateRequest commentRequest, UUID activityId,
        UUID activityTypeId)
    {
        if (commentRequest == null || commentRequest.getComment() == null
            || commentRequest.getComment()
            .isBlank())
        {
            return;
        }

        commentCreateCmd.withRequest(CommentCreateCmd.Request.builder()
                .comment(commentRequest.getComment())
                .isAgreeComment(
                    commentRequest.getIsAgreeComment() != null && commentRequest.getIsAgreeComment())
                .activityId(activityId)
                .activityTypeId(activityTypeId)
                .publishingContextId(commentRequest.getPublishingContextId())
                .build())
            .execute();
    }

    public void createGroupMembershipRelation(GroupMembershipCreateRequest groupMembershipRequest,
        UUID activityId, UUID activityTypeId)
    {
        /*if (groupRequest == null || groupRequest.getName() == null || groupRequest.getName()
            .isBlank())
        {
            return;
        }

        groupCreateCmd.withRequest(GroupCreateCmd.Request.builder()
                .name(groupRequest.getName())
                *//*.action(groupRequest.getAction())
                .regionId(groupRequest.getRegionId())
                .activityId(activityId)
                .activityTypeId(activityTypeId)*//*.build())
            .execute();*/
    }

    public void createConnectionRelation(ConnectionCreateRequest connectionRequest, UUID activityId,
        UUID activityTypeId, UUID accountId)
    {
        RequestState requestState = requestStateService.findByCodeThrow(
            RequestStateCodeEnum.PENDING);

        AccountDto accountDto = accountGetDtoCmd.withRequest(AccountGetDtoCmd.Request.builder()
                .accountId(accountId)
                .build())
            .execute();

        UUID potentialAccountId = connectionRequest.getPotentialFriendAccountId();

        if (connectionRequest.getIsNewConnection() && potentialAccountId == null) {
            potentialAccountId = accountCreateCmd.withRequest(AccountCreateCmd.Request.builder()
                    .username(connectionRequest.getFriendUsername())
                    .password("N/A")
                    .socialNetworkId(accountDto.getSocialNetworkId())
                    .type(AccountTypeEnum.fromValue(connectionRequest.getTypeFriendShip()))
                    .build())
                .execute();
        }

        connectionCreateCmd.withRequest(ConnectionCreateCmd.Request.builder()
                .potentialFriendAccountId(potentialAccountId)
                .action(connectionRequest.getAction())
                .type(connectionRequest.getTypeFriendShip())
                .requestStateId(requestState.getId())
                .activityId(activityId)
                .activityTypeId(activityTypeId)
                .build())
            .execute();
    }

    public void createFollowRelation(FollowCreateRequest followRequest, UUID activityId,
        UUID activityTypeId)
    {
        if (followRequest == null || followRequest.getName() == null || followRequest.getName()
            .isBlank())
        {
            return;
        }

        followCreateCmd.withRequest(FollowCreateCmd.Request.builder()
                .name(followRequest.getName())
                .isFollowing(followRequest.getIsFollowing() != null && followRequest.getIsFollowing())
                .activityId(activityId)
                .activityTypeId(activityTypeId)
                .publishingContextId(followRequest.getPublishingContextId())
                .regionId(followRequest.getRegionId())
                .build())
            .execute();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final ActivityCreateRequest activityRequest;
    }
}
