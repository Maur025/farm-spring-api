package com.kernotec.farm.rest.dto.request.activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.farm.rest.dto.request.comment.CommentCreateRequest;
import com.kernotec.farm.rest.dto.request.follow.FollowCreateRequest;
import com.kernotec.farm.rest.dto.request.friend.FriendCreateRequest;
import com.kernotec.farm.rest.dto.request.group.GroupCreateRequest;
import com.kernotec.farm.rest.dto.request.publishing.PublishingCreateRequest;
import com.kernotec.farm.rest.dto.request.reaction.ReactionCreateRequest;
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
    private GroupCreateRequest group;
    private FriendCreateRequest friend;
    private FollowCreateRequest follow;
}
