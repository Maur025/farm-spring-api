package com.kernotec.farm.parametric.jpa.dto.entity;

import com.kernotec.core.jpa.dto.EntityDto;
import com.kernotec.farm.parametric.jpa.enums.OperatorCodeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperatorDto extends EntityDto {

    private String name;
    private OperatorCodeEnum code;
}
