package com.kernotec.farm.jpa.dto.entity.farm;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.jpa.enums.FarmTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FarmDto extends AuditEntityDto {

    private String name;
    private String code;
    private String description;
    private FarmTypeEnum type;
}
