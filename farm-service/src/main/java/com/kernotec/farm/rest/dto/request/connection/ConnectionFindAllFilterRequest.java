package com.kernotec.farm.rest.dto.request.connection;

import com.kernotec.farm.jpa.enums.FriendActionEnum;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConnectionFindAllFilterRequest {

    private UUID socialNetworkId;
    private UUID accountId;
    private UUID requestStateId;
    private FriendActionEnum action;
}
