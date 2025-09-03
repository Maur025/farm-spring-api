package com.kernotec.farm.activity.rest.dto.response.friend;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.account.rest.dto.response.account.AccountFlatResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class FriendResponse extends EntityResponse {

    private UUID accountId;
    private AccountFlatResponse account;

    private UUID friendAccountId;
    private AccountFlatResponse friendAccount;
}
