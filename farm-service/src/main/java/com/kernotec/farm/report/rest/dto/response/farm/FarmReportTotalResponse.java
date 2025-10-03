package com.kernotec.farm.report.rest.dto.response.farm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.report.rest.dto.response.activity.ActivityTypeTotalResponse;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class FarmReportTotalResponse extends EntityResponse {

    private UUID id;
    private String name;
    private ActivityTypeTotalResponse activityTypeTotals;
}
