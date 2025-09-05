package com.kernotec.farm.activity.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.farm.activity.jpa.entity.GroupMembership;
import com.kernotec.farm.activity.jpa.enums.GroupActionEnum;
import com.kernotec.farm.activity.jpa.service.GroupMembershipService;
import com.kernotec.farm.activity.rest.ApiSpec.GroupMembershipSpec;
import com.kernotec.farm.activity.rest.command.group.membership.ProcessGroupMembershipUpdateRequestCmd;
import com.kernotec.farm.activity.rest.dto.request.group.membership.GroupMembershipFindAllFilterRequest;
import com.kernotec.farm.activity.rest.dto.request.group.membership.GroupMembershipUpdateRequest;
import com.kernotec.farm.activity.rest.dto.response.group.membership.GroupMembershipResponse;
import com.kernotec.farm.activity.rest.mapper.group.membership.GroupMembershipResponseMapper;
import com.kernotec.farm.parametric.jpa.enums.RequestStateCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = GroupMembershipSpec.TAG_NAME, description = GroupMembershipSpec.TAG_DESCRIPTION)
@RequestMapping(path = GroupMembershipSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class GroupMembershipController {

    private final GroupMembershipService groupMembershipService;
    private final GroupMembershipResponseMapper groupMembershipResponseMapper;
    private final ProcessGroupMembershipUpdateRequestCmd processGroupMembershipUpdateRequestCmd;

    @Operation(summary = "Find all group memberships")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<GroupMembershipResponse> findAll(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending,
        @RequestParam(required = false) UUID socialNetworkId,
        @RequestParam(required = false) UUID accountId,
        @RequestParam(required = false) RequestStateCodeEnum requestStateCode,
        @RequestParam(required = false) GroupActionEnum action)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<GroupMembership> groupMembershipPage = groupMembershipService.findAllWithFilters(
            GroupMembershipFindAllFilterRequest.builder()
                .socialNetworkId(socialNetworkId)
                .accountId(accountId)
                .requestStateCode(requestStateCode)
                .action(action)
                .build(), pageable
        );

        return PageResponse.<GroupMembershipResponse>builder()
            .code(HttpStatus.OK.value())
            .data(groupMembershipResponseMapper.toResponse(groupMembershipPage.getContent()))
            .pagination(PaginationResponse.builder()
                .pages(groupMembershipPage.getTotalPages())
                .count(groupMembershipPage.getTotalElements())
                .build())
            .build();
    }

    @Operation(summary = "Find membership by id")
    @GetMapping("{groupMembershipId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<GroupMembershipResponse> findById(
        @PathVariable("groupMembershipId") UUID groupMembershipId)
    {
        GroupMembership groupMembership = groupMembershipService.findByIdThrow(groupMembershipId);

        return SingleResponse.<GroupMembershipResponse>builder()
            .code(HttpStatus.OK.value())
            .data(groupMembershipResponseMapper.toResponse(groupMembership))
            .build();
    }

    @Operation(summary = "Update group membership")
    @PutMapping("{groupMembershipId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<GroupMembershipResponse> update(
        @PathVariable("groupMembershipId") UUID groupMembershipId,
        @RequestBody GroupMembershipUpdateRequest request)
    {
        processGroupMembershipUpdateRequestCmd.withRequest(
                ProcessGroupMembershipUpdateRequestCmd.Request.builder()
                    .groupMembershipId(groupMembershipId)
                    .groupMembershipRequest(request)
                    .build())
            .execute();

        return SingleResponse.<GroupMembershipResponse>builder()
            .code(HttpStatus.OK.value())
            .message("update successfully")
            .build();
    }
}
