package com.kernotec.farm.rest.command.activity;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.command.activity.ActivityUpdateCmd;
import com.kernotec.farm.rest.dto.request.activity.ActivityUpdateRequest;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessActivityUpdateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessActivityUpdateRequestCmd.Request, Void>
{

    private final ActivityUpdateCmd activityUpdateCmd;

    @Override
    protected Void run(Request request) {
        ActivityUpdateRequest activityRequest = request.getActivityRequest();

        activityUpdateCmd.withRequest(ActivityUpdateCmd.Request.builder()
                .activityId(request.getActivityId())
                .link(activityRequest.getLink())
                .activityDate(activityRequest.getActivityDate())
                .accountId(activityRequest.getAccountId())
                .activityTypeId(activityRequest.getActivityTypeId())
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final ActivityUpdateRequest activityRequest;

        @NotNull
        private final UUID activityId;
    }
}
