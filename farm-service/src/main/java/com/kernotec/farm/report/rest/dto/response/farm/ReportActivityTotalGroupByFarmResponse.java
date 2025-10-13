package com.kernotec.farm.report.rest.dto.response.farm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class ReportActivityTotalGroupByFarmResponse extends EntityResponse {

    private List<FarmReportTotalResponse> reportTotalFarms;
}
