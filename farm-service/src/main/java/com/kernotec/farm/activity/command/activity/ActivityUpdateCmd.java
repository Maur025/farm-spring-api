package com.kernotec.farm.activity.command.activity;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.service.ActivityService;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ActivityUpdateCmd extends
    AbstractTransactionalRequiredCommand<ActivityUpdateCmd.Request, Void>
{

    private final ActivityService activityService;

    @Override
    protected Void run(Request request) {
        Activity activity = activityService.findByIdThrow(request.getActivityId());

        if (request.getLink() != null) {
            activity.setLink(request.getLink());
        }
        if (request.getActivityDate() != null) {
            activity.setActivityDate(request.getActivityDate());
        }
        if (request.getAccountId() != null) {
            activity.setAccountId(request.getAccountId());
        }
        if (request.getActivityTypeId() != null) {
            activity.setActivityTypeId(request.getActivityTypeId());
        }

        activityService.save(activity);

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID activityId;

        private final String link;
        private final ZonedDateTime activityDate;
        private final UUID accountId;
        private final UUID activityTypeId;
    }
}
