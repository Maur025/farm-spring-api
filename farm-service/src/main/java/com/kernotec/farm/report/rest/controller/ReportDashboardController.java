package com.kernotec.farm.report.rest.controller;

import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.farm.report.rest.ApiSpec.ReportSpec;
import com.kernotec.farm.report.rest.dto.request.ReportDashboardRequest;
import com.kernotec.farm.report.rest.dto.response.farm.ReportActivityTotalGroupByFarmResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = ReportSpec.TAG_NAME, description = ReportSpec.TAG_DESCRIPTION)
@RequestMapping(path = ReportSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class ReportDashboardController {

    @Operation(summary = "Get activity total details grouped by farms")
    @PostMapping("dashboard/farms/activities/totals")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ReportActivityTotalGroupByFarmResponse> getTotalActivitiesByFarm(
        @RequestBody ReportDashboardRequest request)
    {
        log.info("Hola");
        return SingleResponse.<ReportActivityTotalGroupByFarmResponse>builder()
            .code(HttpStatus.OK.value())
            .build();
    }
}
