package com.kernotec.farm.activity.jpa.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.parametric.jpa.dto.entity.PublishingContextDto;
import com.kernotec.farm.parametric.jpa.dto.entity.RegionDto;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ProfileDto extends AuditEntityDto {

    private String name;

    private UUID regionId;
    private RegionDto region;

    private UUID publishingContextId;
    private PublishingContextDto publishingContext;

    public ProfileDto(UUID id, String profileName, String regionName) {
        this.setId(id);
        this.name = profileName;

        if (regionName != null) {
            this.region = new RegionDto();
            this.region.setName(regionName);
        }
    }
}
