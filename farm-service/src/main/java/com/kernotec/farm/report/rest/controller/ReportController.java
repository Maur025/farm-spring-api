package com.kernotec.farm.report.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.farm.report.jpa.service.AccountSummaryReportService;
import com.kernotec.farm.report.jpa.service.CommentSummaryService;
import com.kernotec.farm.report.jpa.service.FriendSummaryService;
import com.kernotec.farm.report.jpa.service.GroupSummaryService;
import com.kernotec.farm.report.jpa.service.PageSummaryService;
import com.kernotec.farm.report.jpa.service.PublishingSummaryService;
import com.kernotec.farm.report.jpa.service.ReactionSummaryService;
import com.kernotec.farm.report.rest.ApiSpec.ReportSpec;
import com.kernotec.farm.report.rest.dto.request.ActivitySummaryByAccountRequest;
import com.kernotec.farm.report.rest.dto.response.account.ActivitySummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.CommentSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.FriendSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.GroupSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.PageSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.PublishingSummaryResponse;
import com.kernotec.farm.report.rest.dto.response.account.ReactionSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = ReportSpec.TAG_NAME, description = ReportSpec.TAG_DESCRIPTION)
@RequestMapping(path = ReportSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class ReportController {

    private final AccountSummaryReportService accountSummaryReportService;
    private final FriendSummaryService friendSummaryService;
    private final GroupSummaryService groupSummaryService;
    private final PageSummaryService pageSummaryService;
    private final PublishingSummaryService publishingSummaryService;
    private final ReactionSummaryService reactionSummaryService;
    private final CommentSummaryService commentSummaryService;

    @Operation(summary = "Get activity summary by account")
    @PostMapping("account/{accountId}/activities/summary")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ActivitySummaryResponse> getActivitySummaryByAccount(
        @PathVariable("accountId") UUID accountId,
        @RequestBody ActivitySummaryByAccountRequest request)
    {
        return SingleResponse.<ActivitySummaryResponse>builder()
            .code(HttpStatus.OK.value())
            .data(accountSummaryReportService.getActivitySummaryByAccountId(accountId, request))
            .build();
    }

    @Operation(summary = "Get friend summary by account")
    @PostMapping("account/{accountId}/friends/summary")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<FriendSummaryResponse> getFriendSummaryByAccount(
        @PathVariable("accountId") UUID accountId,
        @RequestBody ActivitySummaryByAccountRequest request,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "5") Integer size,
        @RequestParam(defaultValue = "name") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        return SingleResponse.<FriendSummaryResponse>builder()
            .code(HttpStatus.OK.value())
            .data(friendSummaryService.getFriendSummary(
                accountId, request.toBuilder()
                    .pageable(pageable)
                    .build()
            ))
            .build();
    }

    @Operation(summary = "Get group summary by account")
    @PostMapping("account/{accountId}/groups/summary")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<GroupSummaryResponse> getGroupSummaryByAccount(
        @PathVariable("accountId") UUID accountId,
        @RequestBody ActivitySummaryByAccountRequest request,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "5") Integer size,
        @RequestParam(defaultValue = "name") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        return SingleResponse.<GroupSummaryResponse>builder()
            .code(HttpStatus.OK.value())
            .data(groupSummaryService.getGroupSummary(
                accountId, request.toBuilder()
                    .pageable(pageable)
                    .build()
            ))
            .build();
    }

    @Operation(summary = "Get page summary by account")
    @PostMapping("account/{accountId}/pages/summary")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<PageSummaryResponse> getPageSummaryByAccount(
        @PathVariable("accountId") UUID accountId,
        @RequestBody ActivitySummaryByAccountRequest request,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "5") Integer size,
        @RequestParam(defaultValue = "name") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        return SingleResponse.<PageSummaryResponse>builder()
            .code(HttpStatus.OK.value())
            .data(pageSummaryService.getPageSummary(
                accountId, request.toBuilder()
                    .pageable(pageable)
                    .build()
            ))
            .build();
    }

    @Operation(summary = "Get publishing summary by account")
    @PostMapping("account/{accountId}/publications/summary")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<PublishingSummaryResponse> getPublishingSummaryByAccount(
        @PathVariable("accountId") UUID accountId,
        @RequestBody ActivitySummaryByAccountRequest request,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "5") Integer size,
        @RequestParam(defaultValue = "name") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        return SingleResponse.<PublishingSummaryResponse>builder()
            .code(HttpStatus.OK.value())
            .data(publishingSummaryService.getPublishingSummary(
                accountId, request.toBuilder()
                    .pageable(pageable)
                    .build()
            ))
            .build();
    }

    @Operation(summary = "Get reaction summary by account")
    @PostMapping("account/{accountId}/reactions/summary")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ReactionSummaryResponse> getReactionSummaryByAccount(
        @PathVariable("accountId") UUID accountId,
        @RequestBody ActivitySummaryByAccountRequest request)
    {
        return SingleResponse.<ReactionSummaryResponse>builder()
            .code(HttpStatus.OK.value())
            .data(reactionSummaryService.getReactionSummary(accountId, request))
            .build();
    }

    @Operation(summary = "Get comment summary by account")
    @PostMapping("account/{accountId}/comments/summary")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<CommentSummaryResponse> getCommentSummaryByAccount(
        @PathVariable("accountId") UUID accountId,
        @RequestBody ActivitySummaryByAccountRequest request,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "5") Integer size,
        @RequestParam(defaultValue = "name") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);

        return SingleResponse.<CommentSummaryResponse>builder()
            .code(HttpStatus.OK.value())
            .data(commentSummaryService.getCommentSummary(
                accountId, request.toBuilder()
                    .pageable(pageable)
                    .build()
            ))
            .build();
    }
}
