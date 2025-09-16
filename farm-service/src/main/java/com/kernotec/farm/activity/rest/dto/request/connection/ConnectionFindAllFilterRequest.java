package com.kernotec.farm.activity.rest.dto.request.connection;

import com.kernotec.farm.activity.jpa.enums.ConnectionActionEnum;
import com.kernotec.farm.parametric.jpa.enums.RequestStateCodeEnum;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConnectionFindAllFilterRequest {

    private UUID socialNetworkId;
    private UUID accountId;
    private RequestStateCodeEnum requestStateCode;
    private ConnectionActionEnum action;
}
