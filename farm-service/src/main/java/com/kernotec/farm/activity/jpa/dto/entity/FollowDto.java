package com.kernotec.farm.activity.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.parametric.jpa.dto.entity.ActivityTypeDto;
import com.kernotec.farm.parametric.jpa.dto.entity.PublishingContextDto;
import com.kernotec.farm.parametric.jpa.dto.entity.RegionDto;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowDto extends AuditEntityDto {

    private String name;
    private boolean isFollowing;

    private UUID publishingContextId;
    private PublishingContextDto publishingContext;

    private UUID regionId;
    private RegionDto region;

    private UUID activityId;
    private ActivityDto activity;

    private UUID activityTypeId;
    private ActivityTypeDto activityType;
}
