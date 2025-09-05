package com.kernotec.farm.account.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.farm.account.jpa.entity.AccountGroup;
import com.kernotec.farm.account.jpa.service.AccountGroupService;
import com.kernotec.farm.account.rest.ApiSpec.AccountGroupSpec;
import com.kernotec.farm.account.rest.dto.response.account.group.AccountGroupResponse;
import com.kernotec.farm.account.rest.mapper.account.group.AccountGroupResponseMapper;
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

@Tag(name = AccountGroupSpec.TAG_NAME, description = AccountGroupSpec.TAG_DESCRIPTION)
@RequestMapping(path = AccountGroupSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class AccountGroupController {

    private final AccountGroupService accountGroupService;
    private final AccountGroupResponseMapper accountGroupResponseMapper;

    @Operation(summary = "Find all account groups")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<AccountGroupResponse> findAll(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending,
        @RequestParam(required = false) UUID accountId,
        @RequestParam(required = false) UUID socialNetworkId)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<AccountGroup> accountGroupPage = accountGroupService.findAllWithFilters(
            accountId, socialNetworkId, pageable);

        return PageResponse.<AccountGroupResponse>builder()
            .code(HttpStatus.OK.value())
            .data(accountGroupResponseMapper.toResponse(accountGroupPage.getContent()))
            .pagination(PaginationResponse.builder()
                .pages(accountGroupPage.getTotalPages())
                .count(accountGroupPage.getTotalElements())
                .build())
            .build();
    }

    @Operation(summary = "Find account group by id")
    @GetMapping("{accountGroupId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<AccountGroupResponse> findById(
        @PathVariable("accountGroupId") UUID accountGroupId)
    {
        AccountGroup accountGroup = accountGroupService.findByIdThrow(accountGroupId);

        return SingleResponse.<AccountGroupResponse>builder()
            .code(HttpStatus.OK.value())
            .data(accountGroupResponseMapper.toResponse(accountGroup))
            .build();
    }
}
