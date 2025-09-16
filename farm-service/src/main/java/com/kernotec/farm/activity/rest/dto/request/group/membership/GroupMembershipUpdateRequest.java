package com.kernotec.farm.activity.rest.dto.request.group.membership;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.farm.activity.jpa.enums.ResponseRequestStateEnum;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class GroupMembershipUpdateRequest extends BaseRequest {

    private ResponseRequestStateEnum responseRequestState;
    private ZonedDateTime responseDate;
}
