package com.kernotec.farm.activity.rest.command.comment;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.command.comment.CommentCreateCmd;
import com.kernotec.farm.activity.rest.dto.request.comment.CommentCreateRequest;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentCreateActivityRelationCmd extends
    AbstractTransactionalRequiredCommand<CommentCreateActivityRelationCmd.Request, Void>
{

    private final CommentCreateCmd commentCreateCmd;

    @Override
    protected Void run(Request request) {
        CommentCreateRequest commentRequest = request.getCommentRequest();

        if (commentRequest == null || commentRequest.getComment() == null
            || commentRequest.getComment()
            .isBlank())
        {
            log.debug("No comment provided, skipping activity relation creation.");

            return null;
        }

        commentCreateCmd.withRequest(CommentCreateCmd.Request.builder()
                .comment(commentRequest.getComment())
                .isAgreeComment(
                    commentRequest.getIsAgreeComment() != null && commentRequest.getIsAgreeComment())
                .activityId(request.getActivityId())
                .activityTypeId(request.getActivityTypeId())
                .publishingContextId(commentRequest.getPublishingContextId())
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        private final CommentCreateRequest commentRequest;
        private final UUID activityId;
        private final UUID activityTypeId;
    }
}
