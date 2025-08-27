package com.kernotec.farm.command.reaction;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.jpa.entity.Reaction;
import com.kernotec.farm.jpa.service.ReactionService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReactionCreateCmd extends
    AbstractTransactionalRequiredCommand<ReactionCreateCmd.Request, UUID>
{

    private final ReactionService reactionService;

    @Override
    protected UUID run(Request request) {
        Reaction reaction = new Reaction();

        reaction.setReactionTypeId(request.getReactionTypeId());
        reaction.setActivityId(request.getActivityId());
        reaction.setActivityTypeId(request.getActivityTypeId());

        reaction = reactionService.save(reaction);
        return reaction.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID reactionTypeId;
        @NotNull
        private final UUID activityId;
        @NotNull
        private final UUID activityTypeId;
    }
}
