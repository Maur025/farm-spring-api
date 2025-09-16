package com.kernotec.farm.activity.rest.dto.request.group.membership;

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
public class GroupMembershipCreateRequest extends BaseRequest {

    private UUID groupId;

    private String groupName;
    private Boolean isNewGroup;

    private GroupActionEnum action;
    private UUID regionId;
    private UUID publishingContextId;
}
