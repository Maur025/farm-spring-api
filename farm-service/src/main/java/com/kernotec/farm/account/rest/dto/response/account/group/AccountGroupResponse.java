package com.kernotec.farm.account.rest.dto.response.account.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.account.rest.dto.response.account.AccountFlatResponse;
import com.kernotec.farm.activity.rest.dto.response.group.GroupResponse;
import com.kernotec.farm.parametric.rest.dto.response.group.state.GroupStateResponse;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class AccountGroupResponse extends EntityResponse {

    private ZonedDateTime joinedAt;
    private ZonedDateTime leftAt;

    private UUID accountId;
    private AccountFlatResponse account;

    private UUID groupId;
    private GroupResponse group;

    private UUID groupStateId;
    private GroupStateResponse groupState;
}
