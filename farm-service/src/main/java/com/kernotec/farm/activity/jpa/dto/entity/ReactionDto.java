package com.kernotec.farm.activity.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.parametric.jpa.dto.entity.ActivityTypeDto;
import com.kernotec.farm.parametric.jpa.dto.entity.ReactionTypeDto;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReactionDto extends AuditEntityDto {

    private UUID reactionTypeId;
    private ReactionTypeDto reactionType;

    private UUID activityId;
    private ActivityDto activity;

    private UUID activityTypeId;
    private ActivityTypeDto activityType;
}
