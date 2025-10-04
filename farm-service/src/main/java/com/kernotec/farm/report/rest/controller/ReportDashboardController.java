package com.kernotec.farm.report.rest.controller;

import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.farm.report.jpa.service.ReportDashboardService;
import com.kernotec.farm.report.rest.ApiSpec.ReportSpec;
import com.kernotec.farm.report.rest.dto.request.ReportDashboardRequest;
import com.kernotec.farm.report.rest.dto.response.farm.FarmReportTotalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = ReportSpec.TAG_NAME, description = ReportSpec.TAG_DESCRIPTION)
@RequestMapping(path = ReportSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class ReportDashboardController {

    private final ReportDashboardService reportDashboardService;

    @Operation(summary = "Get activity total details grouped by farms")
    @PostMapping("dashboard/farms/activities/totals")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<FarmReportTotalResponse> getTotalActivitiesByFarm(
        @RequestBody ReportDashboardRequest request)
    {
        return PageResponse.<FarmReportTotalResponse>builder()
            .code(HttpStatus.OK.value())
            .data(reportDashboardService.findReportTotalFarms(request))
            .build();
    }
}
