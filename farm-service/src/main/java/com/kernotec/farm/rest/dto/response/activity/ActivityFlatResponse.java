package com.kernotec.farm.rest.dto.response.activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ActivityFlatResponse extends EntityResponse {

    private String link;
    private ZonedDateTime activityDate;

    private UUID accountId;
    private UUID activityTypeId;
}
