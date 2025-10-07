package com.kernotec.farm.report.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.rest.dto.response.activity.ActivityResponse;
import com.kernotec.farm.activity.rest.mapper.activity.ActivityResponseMapper;
import com.kernotec.farm.report.jpa.enums.RatingTypeEnum;
import com.kernotec.farm.report.jpa.enums.ReportDispositionEnum;
import com.kernotec.farm.report.jpa.service.ReportActivityService;
import com.kernotec.farm.report.rest.ApiSpec.ReportSpec;
import com.kernotec.farm.report.rest.command.activity.ReportActivityExcelExportCmd;
import com.kernotec.farm.report.rest.command.activity.ReportActivityPdfExportCmd;
import com.kernotec.farm.report.rest.command.excel.ExcelExportCmd;
import com.kernotec.farm.report.rest.command.rating.ActivityRatingExcelExportCmd;
import com.kernotec.farm.report.rest.dto.request.ReportActivityRequest;
import com.kernotec.farm.report.rest.dto.request.ReportRatingRequest;
import com.kernotec.farm.report.rest.dto.response.activity.ActivityTypeTotalResponse;
import com.kernotec.farm.report.rest.dto.response.rating.ReportRatingGroupListResponse;
import com.kernotec.farm.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = ReportSpec.TAG_NAME, description = ReportSpec.TAG_DESCRIPTION)
@RequestMapping(path = ReportSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class ReportActivityController {

    private final ReportActivityService reportActivityService;
    private final ActivityResponseMapper activityResponseMapper;
    private final ReportActivityExcelExportCmd reportActivityExcelExportCmd;
    private final AuthUtil authUtil;
    private final ReportActivityPdfExportCmd reportActivityPdfExportCmd;
    private final ActivityRatingExcelExportCmd activityRatingExcelExportCmd;
    private final ExcelExportCmd excelExportCmd;

    @Operation(summary = "Activity report with filters")
    @PostMapping("activities/report")
    public PageResponse<ActivityResponse> getActivitiesReport(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "activityDate") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending,
        @RequestBody ReportActivityRequest filterRequest)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<Activity> activityPage = reportActivityService.findAllWithFilters(
            filterRequest, pageable);

        return PageResponse.<ActivityResponse>builder()
            .code(HttpStatus.OK.value())
            .data(activityResponseMapper.toResponse(activityPage.getContent()))
            .pagination(PaginationResponse.builder()
                .pages(activityPage.getTotalPages())
                .count(activityPage.getTotalElements())
                .build())
            .build();
    }

    @Operation(summary = "Activity report with filters (no pagination)")
    @GetMapping("activities/report/unpaginated")
    public PageResponse<ActivityResponse> getActivitiesReportUnpaginated(
        @RequestParam(defaultValue = "activityDate") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending,
        @RequestParam(required = false) UUID userAuthId,
        @RequestParam(required = false) UUID socialNetworkId,
        @RequestParam(required = false) UUID activityTypeId,
        @RequestParam(required = false) UUID farmId, @RequestParam(required = false) UUID deviceId,
        @RequestParam(required = false) UUID accountId,
        @RequestParam(required = false) ZonedDateTime simpleDate,
        @RequestParam(required = false) ZonedDateTime fromDate,
        @RequestParam(required = false) ZonedDateTime toDate,
        @RequestParam(required = false) ZonedDateTime monthDate,
        @RequestParam(required = false) ZonedDateTime yearDate,
        @RequestParam(required = false) String zoneId)
    {
        var filterRequest = new ReportActivityRequest();
        filterRequest.setUserAuthId(userAuthId);
        filterRequest.setSocialNetworkId(socialNetworkId);
        filterRequest.setActivityTypeId(activityTypeId);
        filterRequest.setFarmId(farmId);
        filterRequest.setDeviceId(deviceId);
        filterRequest.setAccountId(accountId);
        filterRequest.setSimpleDate(simpleDate);
        filterRequest.setFromDate(fromDate);
        filterRequest.setToDate(toDate);
        filterRequest.setMonthDate(monthDate);
        filterRequest.setYearDate(yearDate);
        filterRequest.setZoneId(zoneId);

        List<Activity> activityList = reportActivityService.findAllWithFiltersNoPaginated(
            filterRequest, sortBy, descending);

        return PageResponse.<ActivityResponse>builder()
            .code(HttpStatus.OK.value())
            .data(activityResponseMapper.toResponse(activityList))
            .build();
    }

    @Operation(summary = "Activity totals by types with report filters")
    @PostMapping("activities/report/totals")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ActivityTypeTotalResponse> getTotalActivitiesByType(
        @RequestBody ReportActivityRequest request)
    {
        return SingleResponse.<ActivityTypeTotalResponse>builder()
            .code(HttpStatus.OK.value())
            .data(reportActivityService.getTotalActivitiesByType(request))
            .build();
    }

    @Operation(summary = "Export activities report to excel with filters")
    @PostMapping("activities/report/excel")
    @ResponseStatus(HttpStatus.OK)
    public void exportActivitiesReportAsExcel(HttpServletResponse response,
        @RequestBody ReportActivityRequest filterRequest,
        @RequestParam(required = false) String titleReport,
        @RequestParam(defaultValue = "activityDate") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending, Authentication authentication)
    {
        response.setContentType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=report-activities.xlsx");

        excelExportCmd.withRequest(ExcelExportCmd.Request.builder()
                .response(response)
                .reportTitle(titleReport)
                .callback(sheet -> reportActivityExcelExportCmd.withRequest(
                        ReportActivityExcelExportCmd.Request.builder()
                            .sheet(sheet)
                            .filterRequest(filterRequest)
                            .sortBy(sortBy)
                            .descending(descending)
                            .titleReport(titleReport)
                            .authUsername(authUtil.getAuthNameFromAuthentication(authentication))
                            .build())
                    .execute())
                .build())
            .execute();
    }

    @Operation(summary = "Export activities report to pdf with filters")
    @PostMapping("activities/report/pdf")
    @ResponseStatus(HttpStatus.OK)
    public void exportActivitiesReportAsPdf(HttpServletResponse response,
        @RequestBody ReportActivityRequest filterRequest,
        @RequestParam(required = false) String titleReport,
        @RequestParam(defaultValue = "activityDate") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending, Authentication authentication,
        @RequestParam(defaultValue = "inline") ReportDispositionEnum disposition)
    {
        response.setContentType("application/pdf");
        response.setHeader(
            "Content-Disposition", disposition.toString() + "; filename=report-activities.pdf");

        reportActivityPdfExportCmd.withRequest(ReportActivityPdfExportCmd.Request.builder()
                .response(response)
                .titleReport(titleReport)
                .token(authUtil.getAuthTokenFromAuthentication(authentication))
                .authUsername(authUtil.getAuthNameFromAuthentication(authentication))
                .filterRequest(filterRequest)
                .sortBy(sortBy)
                .isDescending(descending)
                .build())
            .execute();
    }

    @Operation(summary = "get rating of activities with filters")
    @PostMapping("activities/ratings/report")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ReportRatingGroupListResponse> getActivitiesRatings(
        @RequestBody ReportRatingRequest request)
    {
        return SingleResponse.<ReportRatingGroupListResponse>builder()
            .code(HttpStatus.OK.value())
            .data(ReportRatingGroupListResponse.builder()
                .ratingMoreActivities(reportActivityService.getActivitiesRatings(request, true))
                .ratingLessActivities(reportActivityService.getActivitiesRatings(request, false))
                .build())
            .build();
    }

    @Operation(summary = "get rating of activities to test pdf report")
    @GetMapping("activities/ratings/report")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ReportRatingGroupListResponse> getActivitiesRatingsToPdf(
        @RequestParam(required = false) RatingTypeEnum ratingType,
        @RequestParam(defaultValue = "5") Integer limit)
    {
        var request = new ReportRatingRequest();
        request.setRatingType(ratingType);
        request.setLimit(limit);

        return SingleResponse.<ReportRatingGroupListResponse>builder()
            .code(HttpStatus.OK.value())
            .data(ReportRatingGroupListResponse.builder()
                .ratingMoreActivities(reportActivityService.getActivitiesRatings(request, true))
                .ratingLessActivities(reportActivityService.getActivitiesRatings(request, false))
                .build())
            .build();
    }

    @Operation(summary = "rating activities with filters export to excel")
    @PostMapping("activities/ratings/excel")
    @ResponseStatus(HttpStatus.OK)
    public void exportActivitiesRatingsToExcel(HttpServletResponse response,
        @RequestBody ReportRatingRequest request, Authentication authentication)
    {

        response.setContentType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
            "Content-Disposition", "attachment; filename=report-activities-ratings.xlsx");

        String reportTitle = "Clasificación de actividades";

        excelExportCmd.withRequest(ExcelExportCmd.Request.builder()
                .response(response)
                .reportTitle(reportTitle)
                .callback(sheet -> activityRatingExcelExportCmd.withRequest(
                        ActivityRatingExcelExportCmd.Request.builder()
                            .sheet(sheet)
                            .reportTitle(reportTitle)
                            .filterRequest(request)
                            .authUsername(authUtil.getAuthNameFromAuthentication(authentication))
                            .build())
                    .execute())
                .build())
            .execute();
    }
}
