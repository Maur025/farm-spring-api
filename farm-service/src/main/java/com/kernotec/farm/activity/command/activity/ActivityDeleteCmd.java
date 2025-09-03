package com.kernotec.farm.activity.command.activity;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.service.ActivityService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ActivityDeleteCmd extends
    AbstractTransactionalRequiredCommand<ActivityDeleteCmd.Request, Void>
{

    private final ActivityService activityService;

    @Override
    protected Void run(Request request) {
        Activity activity = activityService.findByIdThrow(request.getActivityId());

        activityService.delete(activity);

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID activityId;
    }
}
