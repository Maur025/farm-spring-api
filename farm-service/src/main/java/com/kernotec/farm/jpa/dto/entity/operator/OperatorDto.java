package com.kernotec.farm.jpa.dto.entity.operator;

import com.kernotec.core.jpa.dto.EntityDto;
import com.kernotec.farm.jpa.enums.OperatorCodeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperatorDto extends EntityDto {

    private String name;
    private OperatorCodeEnum code;
}
