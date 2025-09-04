package com.kernotec.farm.account.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.farm.account.jpa.entity.Account;
import com.kernotec.farm.account.jpa.service.AccountService;
import com.kernotec.farm.account.rest.ApiSpec.AccountSpec;
import com.kernotec.farm.account.rest.dto.response.account.AccountFlatResponse;
import com.kernotec.farm.account.rest.dto.response.account.AccountResponse;
import com.kernotec.farm.account.rest.mapper.account.AccountFlatResponseMapper;
import com.kernotec.farm.account.rest.mapper.account.AccountResponseMapper;
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

@Tag(name = AccountSpec.TAG_NAME, description = AccountSpec.TAG_DESCRIPTION)
@RequestMapping(path = AccountSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class AccountController {

    private final AccountService accountService;
    private final AccountResponseMapper accountResponseMapper;
    private final AccountFlatResponseMapper accountFlatResponseMapper;

    @Operation(summary = "Find all accounts")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<AccountResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending,
        @RequestParam(required = false) UUID socialNetworkId,
        @RequestParam(required = false) String keyword)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<Account> accountPage = accountService.findAllWithFilters(
            socialNetworkId, keyword, pageable);

        return PageResponse.<AccountResponse>builder()
            .code(HttpStatus.OK.value())
            .data(accountResponseMapper.toResponse(accountPage.getContent()))
            .pagination(PaginationResponse.builder()
                .pages(accountPage.getTotalPages())
                .count(accountPage.getTotalElements())
                .build())
            .build();
    }

    @Operation(summary = "Search accounts by username")
    @GetMapping("search")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<AccountFlatResponse> searchByUsername(@RequestParam String username,
        @RequestParam(required = false) UUID socialNetworkId,
        @RequestParam(required = false) UUID ignoreAccountId)
    {
        Pageable pageable = PageableUtil.of(0, 20, "createdAt", true);
        Page<Account> accountPage = accountService.searchByUsername(
            username, socialNetworkId, ignoreAccountId, pageable);

        return PageResponse.<AccountFlatResponse>builder()
            .code(HttpStatus.OK.value())
            .data(accountFlatResponseMapper.toResponse(accountPage.getContent()))
            .build();
    }

    @Operation(summary = "Find all Accounts unpaginated")
    @GetMapping("unpaginated")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<AccountResponse> findAllUnpaginated() {
        List<Account> accountList = accountService.findAll();

        return PageResponse.<AccountResponse>builder()
            .code(HttpStatus.OK.value())
            .data(accountResponseMapper.toResponse(accountList))
            .build();
    }
}
