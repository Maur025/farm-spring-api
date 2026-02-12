package com.kernotec.farm.activity.rest.dto.request.group.membership;

import com.kernotec.farm.activity.jpa.enums.GroupActionEnum;
import com.kernotec.farm.parametric.jpa.enums.RequestStateCodeEnum;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GroupMembershipFindAllFilterRequest {

    private UUID socialNetworkId;
    private UUID accountId;
    private RequestStateCodeEnum requestStateCode;
    private GroupActionEnum action;
    private String keyword;
}
