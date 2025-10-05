package com.kernotec.farm.report.rest.dto.response.dashboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class ReportDashboardTotalResponse extends EntityResponse {

    private Long totalDevices;
    private Long totalAccounts;
    private Long totalFarms;
}
