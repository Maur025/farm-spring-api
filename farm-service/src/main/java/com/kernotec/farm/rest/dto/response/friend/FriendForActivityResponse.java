package com.kernotec.farm.rest.dto.response.friend;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.jpa.enums.FriendActionEnum;
import com.kernotec.farm.jpa.enums.FriendTypeEnum;
import com.kernotec.farm.rest.dto.response.request.state.RequestStateResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class FriendForActivityResponse extends EntityResponse {

    private String name;
    private FriendActionEnum action;
    private FriendTypeEnum type;

    private UUID requestStateId;
    private RequestStateResponse requestState;
}
