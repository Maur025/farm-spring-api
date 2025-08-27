package com.kernotec.farm.rest.command.activity;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.command.activity.ActivityCreateCmd;
import com.kernotec.farm.rest.dto.request.activity.ActivityCreateRequest;
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

    private final ActivityCreateCmd activityCreateCmd;

    @Override
    protected UUID run(Request request) {
        ActivityCreateRequest activityRequest = request.getActivityRequest();

        return activityCreateCmd.withRequest(ActivityCreateCmd.Request.builder()
                .link(activityRequest.getLink())
                .activityDate(activityRequest.getActivityDate())
                .accountId(activityRequest.getAccountId())
                .activityTypeId(activityRequest.getActivityTypeId())
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
