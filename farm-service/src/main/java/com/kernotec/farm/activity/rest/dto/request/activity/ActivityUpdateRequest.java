package com.kernotec.farm.activity.rest.dto.request.activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ActivityUpdateRequest extends BaseRequest {

    private String link;
    private ZonedDateTime activityDate;
    private UUID accountId;
    private UUID activityTypeId;
}
