package com.kernotec.farm.report.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ReportActivityRequest extends FilterDateRequest {

    private UUID userAuthId;
    private UUID socialNetworkId;
    private UUID activityTypeId;
    private UUID farmId;
    private UUID deviceId;
    private UUID accountId;
    private String searchCriteria;
}
