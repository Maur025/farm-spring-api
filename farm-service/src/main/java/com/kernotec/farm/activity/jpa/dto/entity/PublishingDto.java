package com.kernotec.farm.activity.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.parametric.jpa.dto.entity.ActivityTypeDto;
import com.kernotec.farm.parametric.jpa.dto.entity.PublishingContextDto;
import com.kernotec.farm.parametric.jpa.dto.entity.PublishingTypeDto;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublishingDto extends AuditEntityDto {

    private String description;

    private UUID publishingTypeId;
    private PublishingTypeDto publishingType;

    private UUID publishingContextId;
    private PublishingContextDto publishingContext;

    private UUID activityId;
    private ActivityDto activity;

    private UUID activityTypeId;
    private ActivityTypeDto activityType;
}
