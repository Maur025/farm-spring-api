package com.kernotec.farm.activity.rest.command.activity;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.exception.ActivityException;
import com.kernotec.farm.activity.rest.dto.request.activity.ActivityCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.comment.CommentCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.connection.ConnectionCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.follow.FollowCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.publishing.PublishingCreateRequest;
import com.kernotec.farm.parametric.command.activity.type.ActivityTypeGetDtoCmd;
import com.kernotec.farm.parametric.jpa.dto.entity.ActivityTypeDto;
import com.kernotec.farm.parametric.jpa.enums.ActivityTypeCodeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ActivityCreateRequestValidateCmd extends
    AbstractTransactionalRequiredCommand<ActivityCreateRequestValidateCmd.Request, Void>
{

    private final ActivityTypeGetDtoCmd activityTypeGetDtoCmd;

    @Override
    protected Void run(Request request) {
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
                if (activityRequest.getConnection() == null) {
                    launchExceptionDataMissing("connection");
                }

                ConnectionCreateRequest connection = activityRequest.getConnection();

                if (connection.getAction() == null || activityRequest.getLink() == null
                    || activityRequest.getLink()
                    .isBlank())
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
        return null;
    }

    private void launchExceptionDataMissing(String fieldName) {
        throw new ActivityException(
            "data.missing", "'" + fieldName + "'", HttpStatus.BAD_REQUEST.value());
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final ActivityCreateRequest activityRequest;
    }
}
