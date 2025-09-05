package com.kernotec.farm.activity.rest.command.publishing;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.command.publishing.PublishingCreateCmd;
import com.kernotec.farm.activity.rest.dto.request.publishing.PublishingCreateRequest;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PublishingCreateActivityRelationCmd extends
    AbstractTransactionalRequiredCommand<PublishingCreateActivityRelationCmd.Request, Void>
{

    private final PublishingCreateCmd publishingCreateCmd;

    @Override
    protected Void run(Request request) {
        PublishingCreateRequest publishingRequest = request.getPublishingRequest();

        if (publishingRequest == null || publishingRequest.getPublishingContextId() == null) {
            log.debug(
                "No publishing request or context ID provided, skipping publishing creation.");

            return null;
        }

        publishingCreateCmd.withRequest(PublishingCreateCmd.Request.builder()
                .description(publishingRequest.getDescription())
                .publishingTypeId(publishingRequest.getPublishingTypeId())
                .publishingContextId(publishingRequest.getPublishingContextId())
                .activityId(request.getActivityId())
                .activityTypeId(request.getActivityTypeId())
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        private final PublishingCreateRequest publishingRequest;
        private final UUID activityId;
        private final UUID activityTypeId;
    }
}
