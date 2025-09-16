package com.kernotec.farm.activity.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.parametric.jpa.dto.entity.ActivityTypeDto;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityDto extends AuditEntityDto {

    private String link;
    private ZonedDateTime activityDate;

    private UUID accountId;
    private AccountDto account;

    private UUID activityTypeId;
    private ActivityTypeDto activityType;

    private Set<PublishingDto> publishings;
    private Set<ReactionDto> reactions;
    private Set<CommentDto> comments;
    private Set<GroupMembershipDto> groupMemberships;
    private Set<ConnectionDto> connections;
    private Set<FollowDto> follows;
}
