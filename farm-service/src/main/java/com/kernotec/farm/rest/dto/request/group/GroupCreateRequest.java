package com.kernotec.farm.rest.dto.request.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.farm.activity.jpa.enums.GroupActionEnum;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class GroupCreateRequest extends BaseRequest {

    private String name;
    private GroupActionEnum action;
    private UUID regionId;
}
