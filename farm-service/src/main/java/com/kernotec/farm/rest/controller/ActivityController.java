package com.kernotec.farm.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.farm.jpa.entity.Activity;
import com.kernotec.farm.jpa.service.ActivityService;
import com.kernotec.farm.rest.ApiSpec.ActivitySpec;
import com.kernotec.farm.rest.command.activity.ProcessActivityCreateRequestCmd;
import com.kernotec.farm.rest.command.activity.ProcessActivityDeleteRequestCmd;
import com.kernotec.farm.rest.command.activity.ProcessActivityUpdateRequestCmd;
import com.kernotec.farm.rest.dto.request.activity.ActivityCreateRequest;
import com.kernotec.farm.rest.dto.request.activity.ActivityUpdateRequest;
import com.kernotec.farm.rest.dto.response.activity.ActivityResponse;
import com.kernotec.farm.rest.mapper.activity.ActivityResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = ActivitySpec.TAG_NAME, description = ActivitySpec.TAG_DESCRIPTION)
@RequestMapping(value = ActivitySpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    private final ActivityResponseMapper activityResponseMapper;

    private final ProcessActivityCreateRequestCmd processActivityCreateRequestCmd;
    private final ProcessActivityUpdateRequestCmd processActivityUpdateRequestCmd;
    private final ProcessActivityDeleteRequestCmd processActivityDeleteRequestCmd;

    @Operation(summary = "Find all Activities")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ActivityResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "false") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<Activity> activityPage = activityService.findAll(pageable);

        return PageResponse.<ActivityResponse>builder()
            .code(HttpStatus.OK.value())
            .data(activityResponseMapper.toResponse(activityPage.getContent()))
            .pagination(PaginationResponse.builder()
                .pages(activityPage.getTotalPages())
                .count(activityPage.getTotalElements())
                .build())
            .build();
    }

    @Operation(summary = "Find all Activities no paginated")
    @GetMapping("unpaginated")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ActivityResponse> findAllNoPaginated() {
        List<Activity> activityList = activityService.findAll();

        return PageResponse.<ActivityResponse>builder()
            .code(HttpStatus.OK.value())
            .data(activityResponseMapper.toResponse(activityList))
            .build();
    }

    @Operation(summary = "Find activity by id")
    @GetMapping("{activityId}")
    @ResponseStatus
    public SingleResponse<ActivityResponse> findById(@PathVariable("activityId") UUID activityId) {
        Activity activity = activityService.findByIdThrow(activityId);

        return SingleResponse.<ActivityResponse>builder()
            .code(HttpStatus.OK.value())
            .data(activityResponseMapper.toResponse(activity))
            .build();
    }

    @Operation(summary = "Create activity")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResponse<ActivityResponse> create(@RequestBody ActivityCreateRequest request) {
        UUID activityId = processActivityCreateRequestCmd.withRequest(
                ProcessActivityCreateRequestCmd.Request.builder()
                    .activityRequest(request)
                    .build())
            .execute();

        return SingleResponse.<ActivityResponse>builder()
            .code(HttpStatus.CREATED.value())
            .data(activityResponseMapper.toResponse(activityId))
            .build();
    }

    @Operation(summary = "Update activity")
    @PutMapping("{activityId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ActivityResponse> update(@PathVariable("activityId") UUID activityId,
        @RequestBody ActivityUpdateRequest request)
    {
        processActivityUpdateRequestCmd.withRequest(
                ProcessActivityUpdateRequestCmd.Request.builder()
                    .activityId(activityId)
                    .activityRequest(request)
                    .build())
            .execute();

        return SingleResponse.<ActivityResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Update successful")
            .build();
    }

    @Operation(summary = "Delete activity")
    @DeleteMapping("{activityId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ActivityResponse> delete(@PathVariable("activityId") UUID activityId) {
        processActivityDeleteRequestCmd.withRequest(
                ProcessActivityDeleteRequestCmd.Request.builder()
                    .activityId(activityId)
                    .build())
            .execute();

        return SingleResponse.<ActivityResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Delete successful")
            .build();
    }
}
