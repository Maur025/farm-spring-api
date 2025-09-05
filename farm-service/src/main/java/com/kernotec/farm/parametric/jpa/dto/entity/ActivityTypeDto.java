package com.kernotec.farm.parametric.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityTypeDto extends AuditEntityDto {

    private String name;
    private String code;

    private Set<SocialNetworkDto> socialNetworks;
}
