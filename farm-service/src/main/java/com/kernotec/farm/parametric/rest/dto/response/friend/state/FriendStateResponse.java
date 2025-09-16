package com.kernotec.farm.parametric.rest.dto.response.friend.state;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class FriendStateResponse extends EntityResponse {

    private String name;
    private String code;
}
