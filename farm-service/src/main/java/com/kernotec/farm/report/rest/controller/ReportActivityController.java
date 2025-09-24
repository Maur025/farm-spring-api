package com.kernotec.farm.report.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.farm.activity.jpa.entity.Activity;
import com.kernotec.farm.activity.rest.dto.response.activity.ActivityResponse;
import com.kernotec.farm.activity.rest.mapper.activity.ActivityResponseMapper;
import com.kernotec.farm.report.jpa.service.ReportActivityService;
import com.kernotec.farm.report.rest.ApiSpec.ReportSpec;
import com.kernotec.farm.report.rest.dto.request.ReportActivityRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = ReportSpec.TAG_NAME, description = ReportSpec.TAG_DESCRIPTION)
@RequestMapping(path = ReportSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class ReportActivityController {

    private final ReportActivityService reportActivityService;
    private final ActivityResponseMapper activityResponseMapper;

    @Operation(summary = "Activity report with filters")
    @PostMapping("activities/report")
    public PageResponse<ActivityResponse> getActivitiesReport(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
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
}
