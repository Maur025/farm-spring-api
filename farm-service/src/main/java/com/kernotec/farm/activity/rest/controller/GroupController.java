package com.kernotec.farm.activity.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.farm.activity.jpa.entity.Group;
import com.kernotec.farm.activity.jpa.service.GroupService;
import com.kernotec.farm.activity.rest.ApiSpec.GroupSpec;
import com.kernotec.farm.activity.rest.dto.response.group.GroupResponse;
import com.kernotec.farm.activity.rest.mapper.group.GroupResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = GroupSpec.TAG_NAME, description = GroupSpec.TAG_DESCRIPTION)
@RequestMapping(path = GroupSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class GroupController {

    private final GroupService groupService;
    private final GroupResponseMapper groupResponseMapper;

    @Operation(summary = "Find all groups")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<GroupResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<Group> groupPage = groupService.findAll(pageable);

        return PageResponse.<GroupResponse>builder()
            .code(HttpStatus.OK.value())
            .data(groupResponseMapper.toResponse(groupPage.getContent()))
            .pagination(PaginationResponse.builder()
                .pages(groupPage.getTotalPages())
                .count(groupPage.getTotalElements())
                .build())
            .build();
    }

    @Operation(summary = "Find all groups unpaginated")
    @GetMapping("unpaginated")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<GroupResponse> findAllUnpaginated() {
        Pageable pageable = PageableUtil.of(0, 30, "createdAt", true);
        Page<Group> groupPage = groupService.findAll(pageable);

        return PageResponse.<GroupResponse>builder()
            .code(HttpStatus.OK.value())
            .data(groupResponseMapper.toResponse(groupPage.getContent()))
            .build();
    }

    @Operation(summary = "Find group by id")
    @GetMapping("{groupId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<GroupResponse> findById(@PathVariable UUID groupId) {
        Group group = groupService.findByIdThrow(groupId);

        return SingleResponse.<GroupResponse>builder()
            .code(HttpStatus.OK.value())
            .data(groupResponseMapper.toResponse(group))
            .build();
    }

    @Operation(summary = "Search groups by name containing")
    @GetMapping("search")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<GroupResponse> searchByName(@RequestParam String name,
        @RequestParam(required = false) UUID socialNetworkId)
    {
        Pageable pageable = PageableUtil.of(0, 20, "createdAt", true);
        Page<Group> groupPage = groupService.searchByName(name, socialNetworkId, pageable);

        return PageResponse.<GroupResponse>builder()
            .code(HttpStatus.OK.value())
            .data(groupResponseMapper.toResponse(groupPage.getContent()))
            .build();
    }
}
