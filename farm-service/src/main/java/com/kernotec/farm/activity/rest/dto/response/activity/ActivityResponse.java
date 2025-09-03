package com.kernotec.farm.activity.rest.dto.response.activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.account.rest.dto.response.account.AccountForActivityResponse;
import com.kernotec.farm.parametric.rest.dto.response.activity.type.ActivityTypeForActivityResponse;
import com.kernotec.farm.activity.rest.dto.response.comment.CommentForActivityResponse;
import com.kernotec.farm.activity.rest.dto.response.connection.ConnectionForActivityResponse;
import com.kernotec.farm.activity.rest.dto.response.follow.FollowResponse;
import com.kernotec.farm.activity.rest.dto.response.group.membership.GroupMembershipForActivityResponse;
import com.kernotec.farm.activity.rest.dto.response.publishing.PublishingForActivityResponse;
import com.kernotec.farm.activity.rest.dto.response.reaction.ReactionForActivityResponse;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ActivityResponse extends EntityResponse {

    private String link;
    private ZonedDateTime activityDate;

    private UUID accountId;
    private AccountForActivityResponse account;

    private UUID activityTypeId;
    private ActivityTypeForActivityResponse activityType;

    private Set<PublishingForActivityResponse> publishings;
    private Set<ReactionForActivityResponse> reactions;
    private Set<CommentForActivityResponse> comments;
    private Set<GroupMembershipForActivityResponse> groupMemberships;
    private Set<ConnectionForActivityResponse> connections;
    private Set<FollowResponse> follows;
}
