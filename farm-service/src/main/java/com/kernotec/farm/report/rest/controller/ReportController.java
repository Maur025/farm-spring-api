package com.kernotec.farm.report.rest.controller;

import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.farm.report.jpa.service.AccountReportService;
import com.kernotec.farm.report.rest.ApiSpec.ReportSpec;
import com.kernotec.farm.report.rest.dto.response.account.ActivitySummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = ReportSpec.TAG_NAME, description = ReportSpec.TAG_DESCRIPTION)
@RequestMapping(path = ReportSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class ReportController {

    private final AccountReportService accountReportService;

    @Operation(summary = "Get activity summary by account")
    @GetMapping("account/{accountId}/activities/summary")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ActivitySummaryResponse> getActivitySummaryByAccount(
        @PathVariable("accountId") UUID accountId, @RequestParam UUID socialNetworkId)
    {
        return SingleResponse.<ActivitySummaryResponse>builder()
            .code(HttpStatus.OK.value())
            .data(accountReportService.getActivitySummaryByAccountId(accountId, socialNetworkId))
            .build();
    }
}
