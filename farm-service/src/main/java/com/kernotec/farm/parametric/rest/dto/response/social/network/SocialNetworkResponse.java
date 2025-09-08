package com.kernotec.farm.parametric.rest.dto.response.social.network;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.account.rest.dto.response.account.AccountResponse;
import com.kernotec.farm.parametric.rest.dto.response.activity.type.ActivityTypeResponse;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class SocialNetworkResponse extends EntityResponse {

    private String name;
    private String code;
    private String icon;
    private String color;

    private Set<AccountResponse> accounts;
    private Set<ActivityTypeResponse> activityTypes;
}
