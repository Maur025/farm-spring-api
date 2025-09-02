package com.kernotec.farm.rest.dto.response.friend;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.jpa.enums.FriendActionEnum;
import com.kernotec.farm.jpa.enums.FriendTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class FriendResponse extends EntityResponse {

    private String name;
    private FriendActionEnum status;
    private FriendTypeEnum type;
}
