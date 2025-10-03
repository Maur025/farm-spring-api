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
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class ReportDashboardRequest extends BaseRequest {

    private UUID socialNetworkId;
    private UUID authUserId;
    private ZonedDateTime monthDate;
    private String zoneId;
}
