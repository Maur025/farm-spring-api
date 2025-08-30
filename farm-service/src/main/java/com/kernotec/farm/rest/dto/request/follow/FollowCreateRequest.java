package com.kernotec.farm.rest.dto.request.follow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class FollowCreateRequest extends BaseRequest {

    private String name;
    private Boolean isFollowing;
}
