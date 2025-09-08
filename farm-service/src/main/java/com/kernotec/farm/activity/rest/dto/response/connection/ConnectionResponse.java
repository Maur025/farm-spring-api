package com.kernotec.farm.activity.rest.dto.response.connection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.account.rest.dto.response.account.AccountResponse;
import com.kernotec.farm.activity.jpa.enums.ConnectionActionEnum;
import com.kernotec.farm.activity.jpa.enums.ConnectionTypeEnum;
import com.kernotec.farm.activity.rest.dto.response.activity.ActivityResponse;
import com.kernotec.farm.parametric.rest.dto.response.activity.type.ActivityTypeResponse;
import com.kernotec.farm.parametric.rest.dto.response.request.state.RequestStateResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ConnectionResponse extends EntityResponse {

    private UUID potentialFriendAccountId;
    private AccountResponse potentialFriendAccount;

    private ConnectionActionEnum action;
    private ConnectionTypeEnum type;

    private UUID requestStateId;
    private RequestStateResponse requestState;

    private UUID activityId;
    private ActivityResponse activity;

    private UUID activityTypeId;
    private ActivityTypeResponse activityType;
}
