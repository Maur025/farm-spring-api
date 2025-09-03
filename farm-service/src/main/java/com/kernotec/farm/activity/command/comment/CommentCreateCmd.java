package com.kernotec.farm.activity.command.comment;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.jpa.entity.Comment;
import com.kernotec.farm.activity.jpa.service.CommentService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentCreateCmd extends
    AbstractTransactionalRequiredCommand<CommentCreateCmd.Request, UUID>
{

    private final CommentService commentService;

    @Override
    protected UUID run(Request request) {
        Comment comment = new Comment();

        comment.setComment(request.getComment());
        comment.setAgreeComment(request.isAgreeComment());
        comment.setActivityId(request.getActivityId());
        comment.setActivityTypeId(request.getActivityTypeId());

        comment = commentService.save(comment);
        return comment.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String comment;
        @NotNull
        private final boolean isAgreeComment;
        @NotNull
        private final UUID activityId;
        @NotNull
        private final UUID activityTypeId;
    }
}
