package com.kernotec.farm.command.publishing;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.jpa.entity.Publishing;
import com.kernotec.farm.jpa.service.PublishingService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PublishingCreateCmd extends
    AbstractTransactionalRequiredCommand<PublishingCreateCmd.Request, UUID>
{

    private final PublishingService publishingService;

    @Override
    protected UUID run(Request request) {
        Publishing publishing = new Publishing();

        publishing.setDescription(request.getDescription());
        publishing.setPublishingTypeId(request.getPublishingTypeId());
        publishing.setPublishingContextId(request.getPublishingContextId());
        publishing.setActivityId(request.getActivityId());
        publishing.setActivityTypeId(request.getActivityTypeId());

        publishing = publishingService.save(publishing);
        return publishing.getId();
    }

    @Builder
    @Getter
    public static class Request {

        private final String description;
        @NotNull
        private final UUID publishingTypeId;
        @NotNull
        private final UUID publishingContextId;
        @NotNull
        private final UUID activityId;
        @NotNull
        private final UUID activityTypeId;
    }
}
