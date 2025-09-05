package com.kernotec.farm.activity.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.parametric.jpa.dto.entity.RegionDto;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupDto extends AuditEntityDto {

    private String name;
    private String description;

    private UUID regionId;
    private RegionDto region;

    private Set<GroupMembershipDto> groupMemberships;
}
