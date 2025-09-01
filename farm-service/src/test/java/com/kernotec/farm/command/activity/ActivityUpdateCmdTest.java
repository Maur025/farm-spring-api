package com.kernotec.farm.command.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kernotec.core.test.unit.test.UnitTestWithValidator;
import com.kernotec.core.test.unit.test.util.TestValidatorUtil;
import com.kernotec.core.util.MessageUtil;
import com.kernotec.farm.jpa.entity.Activity;
import com.kernotec.farm.jpa.service.ActivityService;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ActivityUpdateCmdTest extends UnitTestWithValidator {

    @Mock
    private ActivityService activityService;

    @InjectMocks
    private ActivityUpdateCmd activityUpdateCmd;

    private Activity activity;

    @BeforeEach
    public void openMocks() {
        super.openMocks();

        activity = new Activity();
        activity.setId(UUID.randomUUID());
        activity.setLink("http://example.com");
        activity.setActivityDate(ZonedDateTime.now());
        activity.setAccountId(UUID.randomUUID());
        activity.setActivityTypeId(UUID.randomUUID());
    }

    @DisplayName("should update activity successfully")
    @Test
    void testShouldUpdateActivity() {
        when(activityService.findByIdThrow(eq(activity.getId()))).thenReturn(activity);

        Activity activityUpdated = new Activity();
        activityUpdated.setId(activity.getId());
        activityUpdated.setLink("http://other-link.com");
        activityUpdated.setActivityDate(activity.getActivityDate());
        activityUpdated.setAccountId(activity.getAccountId());
        activityUpdated.setActivityTypeId(activity.getActivityTypeId());

        when(activityService.save(any(Activity.class))).thenReturn(activityUpdated);

        activityUpdateCmd.withRequest(ActivityUpdateCmd.Request.builder()
                .activityId(activity.getId())
                .link("http://other-link.com")
                .build())
            .execute();

        verify(activityService).findByIdThrow(eq(activity.getId()));
        verify(activityService).save(any(Activity.class));

        assertEquals("http://other-link.com", activity.getLink());
    }

    @DisplayName("should be error when activityId is null")
    @Test
    void testShouldBeErrorWhenActivityIdIsNull() {
        ActivityUpdateCmd.Request requestCmd = ActivityUpdateCmd.Request.builder()
            .build();

        String message = MessageUtil.getMessageValidation("not.null", Locale.getDefault());
        Map<String, String> expectedViolations = Map.of("activityId", message);

        TestValidatorUtil.validateRequestCmd(validator, requestCmd, expectedViolations);

        verify(activityService, never()).findByIdThrow(any(UUID.class));
    }
}