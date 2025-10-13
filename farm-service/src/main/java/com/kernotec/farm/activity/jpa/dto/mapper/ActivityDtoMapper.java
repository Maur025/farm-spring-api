package com.kernotec.farm.activity.jpa.dto.mapper;

import com.kernotec.farm.account.jpa.dto.mapper.AccountDtoActivityMapper;
import com.kernotec.farm.activity.jpa.dto.entity.ActivityDto;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.audit.user.mapper.AuthUserDataDtoMapper;
import com.kernotec.farm.parametric.jpa.dto.mapper.ActivityTypeDtoFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {AccountDtoActivityMapper.class, ActivityTypeDtoFlatMapper.class,
    PublishingDtoActivityMapper.class, ReactionDtoActivityMapper.class,
    CommentDtoActivityMapper.class, GroupMemberDtoActivityMapper.class,
    ConnectionDtoActivityMapper.class, FollowDtoActivityMapper.class, AuthUserDataDtoMapper.class})
public interface ActivityDtoMapper {

    ActivityDto toDto(Activity activity);

    List<ActivityDto> toDto(List<Activity> activityList);

    Set<ActivityDto> toDto(Set<Activity> activitySet);
}
