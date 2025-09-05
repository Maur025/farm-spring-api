package com.kernotec.farm.activity.rest.command.reaction;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.command.reaction.ReactionCreateCmd;
import com.kernotec.farm.activity.rest.dto.request.reaction.ReactionCreateRequest;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReactionCreateActivityRelationCmd extends
    AbstractTransactionalRequiredCommand<ReactionCreateActivityRelationCmd.Request, Void>
{

    private final ReactionCreateCmd reactionCreateCmd;

    @Override
    protected Void run(Request request) {
        ReactionCreateRequest reactionRequest = request.getReactionRequest();

        if (reactionRequest == null || reactionRequest.getReactionTypeId() == null) {
            log.debug("No reaction type provided, skipping activity relation creation.");

            return null;
        }

        reactionCreateCmd.withRequest(ReactionCreateCmd.Request.builder()
                .reactionTypeId(reactionRequest.getReactionTypeId())
                .activityId(request.getActivityId())
                .activityTypeId(request.getActivityTypeId())
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        private final ReactionCreateRequest reactionRequest;
        private final UUID activityId;
        private final UUID activityTypeId;
    }
}
