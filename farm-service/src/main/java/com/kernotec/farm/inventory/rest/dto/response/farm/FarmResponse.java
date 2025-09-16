package com.kernotec.farm.inventory.rest.dto.response.farm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.inventory.jpa.enums.FarmTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class FarmResponse extends EntityResponse {

    private String name;
    private String code;
    private String description;
    private FarmTypeEnum type;
}
