package com.kernotec.farm.activity.rest.mapper.activity;

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
import com.kernotec.farm.parametric.rest.mapper.activity.type.ActivityTypeResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {AccountResponseForActivityMapper.class, ActivityTypeResponseFlatMapper.class,
    PublishingResponseForActivityMapper.class, ReactionResponseForActivityMapper.class,
    CommentResponseForActivityMapper.class, GroupMemberResponseForActivityMapper.class,
    ConnectionResponseForActivityMapper.class, FollowResponseForActivityMapper.class,
    AuthUserDataResponseMapper.class})
public interface ActivityResponseMapper {

    ActivityResponse toResponse(UUID id);

    ActivityResponse toResponse(Activity activity);

    List<ActivityResponse> toResponse(List<Activity> activityList);

    Set<ActivityResponse> toResponse(Set<Activity> activitySet);
}
