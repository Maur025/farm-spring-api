package com.kernotec.farm.rest.dto.response.group.membership;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.jpa.enums.GroupActionEnum;
import com.kernotec.farm.rest.dto.response.group.GroupResponse;
import com.kernotec.farm.rest.dto.response.publishing.context.PublishingContextResponse;
import com.kernotec.farm.rest.dto.response.region.RegionResponse;
import com.kernotec.farm.rest.dto.response.request.state.RequestStateResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class GroupMembershipForActivityResponse extends EntityResponse {

    private GroupActionEnum action;

    private UUID groupId;
    private GroupResponse group;

    private UUID regionId;
    private RegionResponse region;

    private UUID publishingContextId;
    private PublishingContextResponse publishingContext;

    private UUID requestStateId;
    private RequestStateResponse requestState;
}
