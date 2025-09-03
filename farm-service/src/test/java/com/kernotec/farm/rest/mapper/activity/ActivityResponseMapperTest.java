package com.kernotec.farm.rest.mapper.activity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kernotec.core.test.unit.test.util.AbstractMapperTest;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.rest.dto.response.activity.ActivityResponse;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;

class ActivityResponseMapperTest extends AbstractMapperTest<Activity, ActivityResponse> {

    @InjectMocks
    private ActivityResponseMapperImpl activityResponseMapper;

    private Activity activity;

    @BeforeEach
    public void openMocks() {
        super.openMocks();

        activity = new Activity();
        activity.setId(UUID.randomUUID());

        activity.setLink("https://example.com");
        activity.setActivityDate(ZonedDateTime.now());

        activity.setAccountId(UUID.randomUUID());
        activity.setAccount(new Account());

        activity.setActivityTypeId(UUID.randomUUID());
        activity.setActivityType(new ActivityType());

        setInputObject(activity);
    }

    @Override
    protected ActivityResponse executeMapper(Activity activity) {
        return activityResponseMapper.toResponse(activity);
    }

    @Override
    protected List<ActivityResponse> executeMapper(List<Activity> activityList) {
        return activityResponseMapper.toResponse(activityList);
    }

    @Override
    protected Set<ActivityResponse> executeMapper(Set<Activity> activitySet) {
        return activityResponseMapper.toResponse(activitySet);
    }

    @Override
    protected void assertsForResult(ActivityResponse activityResponse) {
        assertAll(
            "activity response mapper assertions", () -> assertNotNull(activityResponse.getId()),
            () -> assertEquals(activity.getId(), activityResponse.getId()),

            () -> assertEquals(activity.getLink(), activityResponse.getLink()),
            () -> assertEquals(activity.getActivityDate(), activityResponse.getActivityDate()),

            () -> assertEquals(activity.getAccountId(), activityResponse.getAccountId()),

            () -> assertEquals(activity.getActivityTypeId(), activityResponse.getActivityTypeId())
        );
    }
}