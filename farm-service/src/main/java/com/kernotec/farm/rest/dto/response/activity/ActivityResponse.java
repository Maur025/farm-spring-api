package com.kernotec.farm.rest.dto.response.activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.rest.dto.response.PublishingResponse;
import com.kernotec.farm.rest.dto.response.account.AccountForActivityResponse;
import com.kernotec.farm.rest.dto.response.activity.type.ActivityTypeResponse;
import com.kernotec.farm.rest.dto.response.comment.CommentResponse;
import com.kernotec.farm.rest.dto.response.follow.FollowResponse;
import com.kernotec.farm.rest.dto.response.friend.FriendResponse;
import com.kernotec.farm.rest.dto.response.group.GroupResponse;
import com.kernotec.farm.rest.dto.response.reaction.ReactionResponse;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ActivityResponse extends EntityResponse {

    private String link;
    private LocalDateTime activityDate;

    private UUID accountId;
    private AccountForActivityResponse account;

    private UUID activityTypeId;
    private ActivityTypeResponse activityType;

    private Set<PublishingResponse> publishings;
    private Set<ReactionResponse> reactions;
    private Set<CommentResponse> comments;
    private Set<GroupResponse> groups;
    private Set<FriendResponse> friends;
    private Set<FollowResponse> follows;
}
