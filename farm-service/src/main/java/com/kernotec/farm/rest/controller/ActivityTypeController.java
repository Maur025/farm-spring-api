package com.kernotec.farm.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.farm.parametric.jpa.entity.ActivityType;
import com.kernotec.farm.parametric.jpa.service.ActivityTypeService;
import com.kernotec.farm.rest.ApiSpec.ActivityTypeSpec;
import com.kernotec.farm.rest.dto.response.activity.type.ActivityTypeResponse;
import com.kernotec.farm.rest.mapper.activity.type.ActivityTypeResponseFlatMapper;
import com.kernotec.farm.rest.mapper.activity.type.ActivityTypeResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = ActivityTypeSpec.TAG_NAME, description = ActivityTypeSpec.TAG_DESCRIPTION)
@RequestMapping(path = ActivityTypeSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class ActivityTypeController {

    private final ActivityTypeService activityTypeService;
    private final ActivityTypeResponseMapper activityTypeResponseMapper;
    private final ActivityTypeResponseFlatMapper activityTypeResponseFlatMapper;

    @Operation(summary = "Find All Activity Types")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ActivityTypeResponse> findAll(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "false") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<ActivityType> activityTypePage = activityTypeService.findAll(pageable);

        return PageResponse.<ActivityTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(activityTypeResponseMapper.toResponse(activityTypePage.getContent()))
            .pagination(PaginationResponse.builder()
                .pages(activityTypePage.getTotalPages())
                .count(activityTypePage.getTotalElements())
                .build())
            .build();
    }

    @Operation(summary = "Find all Activity types unpaginated")
    @GetMapping("unpaginated")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ActivityTypeResponse> findAllUnpaginated(
        @RequestParam(required = false) UUID socialNetworkId)
    {
        List<ActivityType> activityTypeList = activityTypeService.findAllWithFilters(
            socialNetworkId);

        return PageResponse.<ActivityTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(socialNetworkId == null ? activityTypeResponseMapper.toResponse(activityTypeList)
                : activityTypeResponseFlatMapper.toResponse(activityTypeList))
            .build();
    }
}
