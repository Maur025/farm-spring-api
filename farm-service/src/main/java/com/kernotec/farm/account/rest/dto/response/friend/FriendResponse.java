package com.kernotec.farm.account.rest.dto.response.friend;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.account.rest.dto.response.account.AccountResponse;
import com.kernotec.farm.parametric.rest.dto.response.friend.state.FriendStateResponse;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class FriendResponse extends EntityResponse {

    private ZonedDateTime acceptedAt;
    private ZonedDateTime endedAt;

    private UUID accountId;
    private AccountResponse account;

    private UUID friendAccountId;
    private AccountResponse friendAccount;

    private UUID friendStateId;
    private FriendStateResponse friendState;
}
