package com.kernotec.farm.rest.command.activity;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.command.activity.ActivityDeleteCmd;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessActivityDeleteRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessActivityDeleteRequestCmd.Request, Void>
{

    private final ActivityDeleteCmd activityDeleteCmd;

    @Override
    protected Void run(Request request) {
        activityDeleteCmd.withRequest(ActivityDeleteCmd.Request.builder()
                .activityId(request.getActivityId())
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID activityId;
    }
}
