package com.kernotec.farm.activity.rest.mapper.activity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kernotec.core.test.unit.test.util.AbstractMapperTest;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.rest.mapper.account.AccountResponseForActivityMapper;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.rest.dto.response.activity.ActivityResponse;
import com.kernotec.farm.activity.rest.mapper.comment.CommentResponseForActivityMapper;
import com.kernotec.farm.activity.rest.mapper.connection.ConnectionResponseForActivityMapper;
import com.kernotec.farm.activity.rest.mapper.follow.FollowResponseForActivityMapper;
import com.kernotec.farm.activity.rest.mapper.group.membership.GroupMemberResponseForActivityMapper;
import com.kernotec.farm.activity.rest.mapper.publishing.PublishingResponseForActivityMapper;
import com.kernotec.farm.activity.rest.mapper.reaction.ReactionResponseForActivityMapper;
import com.kernotec.farm.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.parametric.rest.mapper.activity.type.ActivityTypeResponseFlatMapper;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ActivityResponseMapperTest extends AbstractMapperTest<Activity, ActivityResponse> {

    @Mock
    private AccountResponseForActivityMapper accountResponseForActivityMapper;
    @Mock
    private ActivityTypeResponseFlatMapper activityTypeResponseFlatMapper;
    @Mock
    private PublishingResponseForActivityMapper publishingResponseForActivityMapper;
    @Mock
    private ReactionResponseForActivityMapper reactionResponseForActivityMapper;
    @Mock
    private CommentResponseForActivityMapper commentResponseForActivityMapper;
    @Mock
    private GroupMemberResponseForActivityMapper groupMemberResponseForActivityMapper;
    @Mock
    private ConnectionResponseForActivityMapper connectionResponseForActivityMapper;
    @Mock
    private FollowResponseForActivityMapper followResponseForActivityMapper;
    @Mock
    private AuthUserDataResponseMapper authUserDataResponseMapper;

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