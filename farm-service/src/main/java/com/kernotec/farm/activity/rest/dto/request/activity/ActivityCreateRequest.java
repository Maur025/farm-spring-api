package com.kernotec.farm.activity.rest.dto.request.activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.farm.activity.rest.dto.request.comment.CommentCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.connection.ConnectionCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.follow.FollowCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.group.membership.GroupMembershipCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.publishing.PublishingCreateRequest;
import com.kernotec.farm.activity.rest.dto.request.reaction.ReactionCreateRequest;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ActivityCreateRequest {

    private String link;
    private ZonedDateTime activityDate;
    private UUID accountId;
    private UUID activityTypeId;

    private PublishingCreateRequest publishing;
    private ReactionCreateRequest reaction;
    private CommentCreateRequest comment;
    private GroupMembershipCreateRequest groupMembership;
    private ConnectionCreateRequest connection;
    private FollowCreateRequest follow;
}
