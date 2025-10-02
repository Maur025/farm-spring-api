package com.kernotec.farm.report.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ReportActivityRequest extends BaseRequest {

    private UUID userAuthId;
    private UUID socialNetworkId;
    private UUID activityTypeId;
    private UUID farmId;
    private UUID deviceId;
    private UUID accountId;
    private ZonedDateTime simpleDate;
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    private ZonedDateTime monthDate;
    private ZonedDateTime yearDate;
    private String zoneId;
    private String searchCriteria;
}
