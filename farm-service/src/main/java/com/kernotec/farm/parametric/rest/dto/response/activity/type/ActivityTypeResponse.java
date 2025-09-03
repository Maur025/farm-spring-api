package com.kernotec.farm.parametric.rest.dto.response.activity.type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.parametric.rest.dto.response.social.network.SocialNetworkResponse;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ActivityTypeResponse extends EntityResponse {

    private String name;
    private String code;

    private Set<SocialNetworkResponse> socialNetworks;
}
