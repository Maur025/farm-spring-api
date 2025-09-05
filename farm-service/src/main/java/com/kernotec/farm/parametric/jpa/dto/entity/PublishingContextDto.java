package com.kernotec.farm.parametric.jpa.dto.entity;

import com.kernotec.core.jpa.dto.EntityDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublishingContextDto extends EntityDto {

    private String name;
    private String code;
}
