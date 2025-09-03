package com.kernotec.farm.activity.rest.dto.request.connection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.farm.activity.jpa.enums.FriendActionEnum;
import com.kernotec.farm.activity.jpa.enums.FriendTypeEnum;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ConnectionCreateRequest extends BaseRequest {

    private UUID potentialFriendAccountId;

    private String friendUsername;
    private Boolean isNewConnection;

    private FriendActionEnum action;
    private FriendTypeEnum typeFriendShip;
}
