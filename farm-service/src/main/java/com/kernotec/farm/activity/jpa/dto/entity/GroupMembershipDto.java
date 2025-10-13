package com.kernotec.farm.activity.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.activity.jpa.enums.GroupActionEnum;
import com.kernotec.farm.parametric.jpa.dto.entity.ActivityTypeDto;
import com.kernotec.farm.parametric.jpa.dto.entity.RequestStateDto;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupMembershipDto extends AuditEntityDto {

    private GroupActionEnum action;

    private UUID groupId;
    private GroupDto group;

    private UUID requestStateId;
    private RequestStateDto requestState;

    private UUID activityId;
    private ActivityDto activity;

    private UUID activityTypeId;
    private ActivityTypeDto activityType;
}
