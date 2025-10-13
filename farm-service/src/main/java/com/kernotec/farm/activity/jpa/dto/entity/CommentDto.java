package com.kernotec.farm.activity.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.parametric.jpa.dto.entity.ActivityTypeDto;
import com.kernotec.farm.parametric.jpa.dto.entity.PublishingContextDto;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto extends AuditEntityDto {

    private String comment;
    private boolean isAgreeComment;

    private UUID activityId;
    private ActivityDto activity;

    private UUID activityTypeId;
    private ActivityTypeDto activityType;

    private UUID publishingContextId;
    private PublishingContextDto publishingContext;
}
