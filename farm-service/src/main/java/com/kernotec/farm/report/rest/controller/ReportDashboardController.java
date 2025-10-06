package com.kernotec.farm.report.rest.controller;

import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.farm.report.jpa.service.CommentSummaryService;
import com.kernotec.farm.report.jpa.service.GroupSummaryService;
import com.kernotec.farm.report.jpa.service.PublishingSummaryService;
import com.kernotec.farm.report.jpa.service.ReactionSummaryService;
import com.kernotec.farm.report.jpa.service.ReportActivityService;
import com.kernotec.farm.report.jpa.service.ReportDashboardService;
import com.kernotec.farm.report.rest.ApiSpec.ReportSpec;
import com.kernotec.farm.report.rest.dto.request.ActivitySummaryByAccountRequest;
import com.kernotec.farm.report.rest.dto.request.ReportDashboardRequest;
import com.kernotec.farm.report.rest.dto.response.account.GroupSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.PublishingSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.ReactionSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.comment.CommentGroupContextSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.dashboard.ReportActivityYearSummary;
import com.kernotec.farm.report.rest.dto.response.dashboard.ReportDashboardTotalResponse;
import com.kernotec.farm.report.rest.dto.response.farm.FarmReportTotalResponse;
import com.kernotec.farm.report.rest.dto.response.social.network.ReportActivityGroupBySocialNetworkResponse;
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
    private final ReactionSummaryService reactionSummaryService;
    private final ReportActivityService reportActivityService;
    private final PublishingSummaryService publishingSummaryService;
    private final CommentSummaryService commentSummaryService;
    private final GroupSummaryService groupSummaryService;

    @Operation(summary = "Get activity total details grouped by farms")
    @PostMapping("dashboard/farms/activities/total")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<FarmReportTotalResponse> getTotalActivitiesByFarm(
        @RequestBody ReportDashboardRequest request)
    {
        return PageResponse.<FarmReportTotalResponse>builder()
            .code(HttpStatus.OK.value())
            .data(reportDashboardService.findReportTotalFarms(request))
            .build();
    }

    @Operation(summary = "Get total reactions with filter")
    @PostMapping("dashboard/reactions/total")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ReactionSummaryResponse> getTotalReactions(
        @RequestBody ReportDashboardRequest request)
    {

        return SingleResponse.<ReactionSummaryResponse>builder()
            .code(HttpStatus.OK.value())
            .data(reactionSummaryService.getReactionSummary(
                null, ActivitySummaryByAccountRequest.builder()
                    .socialNetworkId(request.getSocialNetworkId())
                    .authUserId(request.getAuthUserId())
                    .monthDate(request.getMonthDate())
                    .zoneId(request.getZoneId())
                    .build()
            ))
            .build();
    }

    @Operation(summary = "total Activities grouped by Social Networks")
    @PostMapping("dashboard/activities/social-networks/total")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ReportActivityGroupBySocialNetworkResponse> getTotalActivitiesBySocialNetworks(
        @RequestBody ReportDashboardRequest request)
    {
        return SingleResponse.<ReportActivityGroupBySocialNetworkResponse>builder()
            .code(HttpStatus.OK.value())
            .data(ReportActivityGroupBySocialNetworkResponse.builder()
                .totalActivities(reportDashboardService.countActivitiesWithFilters(request))
                .socialNetworks(reportDashboardService.findActivitiesGroupBySocialNetwork(request))
                .build())
            .build();
    }

    @Operation(summary = "total dashboard summary")
    @PostMapping("dashboard/summary/total")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ReportDashboardTotalResponse> getDashboardTotalSummary(
        @RequestBody ReportDashboardRequest request)
    {
        return SingleResponse.<ReportDashboardTotalResponse>builder()
            .code(HttpStatus.OK.value())
            .data(ReportDashboardTotalResponse.builder()
                .totalDevices(reportDashboardService.countTotalDevices())
                .totalFarms(reportDashboardService.countTotalFarms())
                .totalAccounts(reportDashboardService.countTotalAccounts(request))
                .build())
            .build();
    }

    @Operation(summary = "total yearly of activities")
    @PostMapping("dashboard/activities/yearly/total")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ReportActivityYearSummary> getTotalYearlyActivities(
        @RequestBody ReportDashboardRequest request)
    {
        return SingleResponse.<ReportActivityYearSummary>builder()
            .code(HttpStatus.OK.value())
            .data(reportDashboardService.getTotalYearlyActivities(request))
            .build();
    }

    @Operation(summary = "total publications grouped by publishing context")
    @PostMapping("dashboard/activities/publications/total")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<PublishingSummaryResponse> getTotalPublicationsByContext(
        @RequestBody ReportDashboardRequest request)
    {
        var filterRequest = ActivitySummaryByAccountRequest.builder()
            .socialNetworkId(request.getSocialNetworkId())
            .authUserId(request.getAuthUserId())
            .monthDate(request.getMonthDate())
            .zoneId(request.getZoneId())
            .build();

        return SingleResponse.<PublishingSummaryResponse>builder()
            .code(HttpStatus.OK.value())
            .data(PublishingSummaryResponse.builder()
                .totalPublications(
                    reportActivityService.getTotalActivitiesByTypeToSummary(
                        null, filterRequest, "publishings"))
                .totalsByContext(
                    publishingSummaryService.getTotalPublicationsByContext(null, filterRequest))
                .build())
            .build();
    }

    @Operation(summary = "total comments grouped by publishing context")
    @PostMapping("dashboard/activities/comments/total")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<CommentGroupContextSummaryResponse> getTotalCommentsByContext(
        @RequestBody ReportDashboardRequest request)
    {
        return SingleResponse.<CommentGroupContextSummaryResponse>builder()
            .code(HttpStatus.OK.value())
            .data(CommentGroupContextSummaryResponse.builder()
                .totalComments(reportActivityService.getTotalActivitiesByTypeToSummary(
                    null, ActivitySummaryByAccountRequest.builder()
                        .socialNetworkId(request.getSocialNetworkId())
                        .authUserId(request.getAuthUserId())
                        .monthDate(request.getMonthDate())
                        .zoneId(request.getZoneId())
                        .build(), "comments"
                ))
                .totalsByContext(commentSummaryService.getTotalCommentsByContext(request))
                .build())
            .build();
    }

    @Operation(summary = "total activity groups group by region ")
    @PostMapping("dashboard/activities/groups/total")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<GroupSummaryResponse> getTotalGroupsByRegion(
        @RequestBody ReportDashboardRequest request)
    {

        return SingleResponse.<GroupSummaryResponse>builder()
            .code(HttpStatus.OK.value())
            .data(GroupSummaryResponse.builder()
                .totalGroups(reportActivityService.getTotalActivitiesByTypeToSummary(
                    null, ActivitySummaryByAccountRequest.builder()
                        .socialNetworkId(request.getSocialNetworkId())
                        .authUserId(request.getAuthUserId())
                        .monthDate(request.getMonthDate())
                        .zoneId(request.getZoneId())
                        .build(), "groupMemberships"
                ))
                .totalsByRegion(groupSummaryService.getTotalActivitiesGroupByRegion(request))
                .build())
            .build();
    }
}
