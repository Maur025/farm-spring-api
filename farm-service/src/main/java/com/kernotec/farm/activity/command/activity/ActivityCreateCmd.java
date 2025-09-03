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
public class ActivityCreateCmd extends
    AbstractTransactionalRequiredCommand<ActivityCreateCmd.Request, UUID>
{

    private final ActivityService activityService;

    @Override
    protected UUID run(Request request) {
        Activity activity = new Activity();

        activity.setLink(request.getLink());
        activity.setActivityDate(request.getActivityDate());
        activity.setAccountId(request.getAccountId());
        activity.setActivityTypeId(request.getActivityTypeId());

        activity = activityService.save(activity);
        return activity.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull()
        private final String link;
        @NotNull
        private final ZonedDateTime activityDate;
        @NotNull
        private final UUID accountId;
        @NotNull
        private final UUID activityTypeId;
    }
}
