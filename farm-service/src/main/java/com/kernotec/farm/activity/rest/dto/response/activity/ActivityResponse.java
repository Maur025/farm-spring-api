package com.kernotec.farm.activity.rest.dto.response.activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.farm.account.rest.dto.response.account.AccountResponse;
import com.kernotec.farm.activity.rest.dto.response.comment.CommentResponse;
import com.kernotec.farm.activity.rest.dto.response.connection.ConnectionResponse;
import com.kernotec.farm.activity.rest.dto.response.follow.FollowResponse;
import com.kernotec.farm.activity.rest.dto.response.group.membership.GroupMembershipResponse;
import com.kernotec.farm.activity.rest.dto.response.publishing.PublishingResponse;
import com.kernotec.farm.activity.rest.dto.response.reaction.ReactionResponse;
import com.kernotec.farm.audit.user.dto.response.DataAuditEntityResponse;
import com.kernotec.farm.parametric.rest.dto.response.activity.type.ActivityTypeResponse;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ActivityResponse extends DataAuditEntityResponse {

    private String link;
    private ZonedDateTime activityDate;

    private UUID accountId;
    private AccountResponse account;

    private UUID activityTypeId;
    private ActivityTypeResponse activityType;

    private Set<PublishingResponse> publishings;
    private Set<ReactionResponse> reactions;
    private Set<CommentResponse> comments;
    private Set<GroupMembershipResponse> groupMemberships;
    private Set<ConnectionResponse> connections;
    private Set<FollowResponse> follows;
}
