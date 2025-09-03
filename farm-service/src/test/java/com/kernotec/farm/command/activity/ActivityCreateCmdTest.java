package com.kernotec.farm.command.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kernotec.core.test.unit.test.UnitTestWithValidator;
import com.kernotec.core.test.unit.test.util.TestValidatorUtil;
import com.kernotec.core.util.MessageUtil;
import com.kernotec.farm.activity.command.activity.ActivityCreateCmd;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.jpa.service.ActivityService;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ActivityCreateCmdTest extends UnitTestWithValidator {

    @Mock
    private ActivityService activityService;

    @InjectMocks
    private ActivityCreateCmd activityCreateCmd;

    private Activity activity;

    @BeforeEach
    public void openMocks() {
        super.openMocks();

        activity = new Activity();
        activity.setId(UUID.randomUUID());
    }

    @DisplayName("should create activity successfully")
    @Test
    void testShouldCreateActivitySuccessfully() {
        when(activityService.save(any(Activity.class))).thenReturn(activity);

        UUID activityId = activityCreateCmd.withRequest(ActivityCreateCmd.Request.builder()
                .link("http://example.com")
                .activityDate(ZonedDateTime.now())
                .accountId(UUID.randomUUID())
                .activityTypeId(UUID.randomUUID())
                .build())
            .execute();

        verify(activityService).save(any(Activity.class));
        assertEquals(activity.getId(), activityId);
    }

    @DisplayName("should be error when all fields are null")
    @Test
    void testShouldBeErrorWhenAllFieldsAreNull() {
        ActivityCreateCmd.Request requestCmd = ActivityCreateCmd.Request.builder()
            .build();

        String message = MessageUtil.getMessageValidation("not.null", Locale.getDefault());
        Map<String, String> expectedViolations = new HashMap<>();
        expectedViolations.put("link", message);
        expectedViolations.put("activityDate", message);
        expectedViolations.put("accountId", message);
        expectedViolations.put("activityTypeId", message);

        TestValidatorUtil.validateRequestCmd(validator, requestCmd, expectedViolations);
    }
}