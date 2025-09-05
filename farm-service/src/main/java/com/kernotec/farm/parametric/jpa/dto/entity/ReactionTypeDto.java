package com.kernotec.farm.parametric.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReactionTypeDto extends AuditEntityDto {

    private String name;
    private String code;

    private UUID socialNetworkId;
    private SocialNetworkDto socialNetwork;
}
