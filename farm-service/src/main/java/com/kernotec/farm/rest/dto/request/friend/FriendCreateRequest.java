package com.kernotec.farm.rest.dto.request.friend;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.farm.jpa.enums.FriendStatusEnum;
import com.kernotec.farm.jpa.enums.FriendTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class FriendCreateRequest extends BaseRequest {

    private String friendName;
    private FriendStatusEnum status;
    private FriendTypeEnum typeFriendShip;
}
