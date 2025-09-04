package com.kernotec.farm.activity.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.farm.activity.jpa.entity.Connection;
import com.kernotec.farm.activity.jpa.enums.ConnectionActionEnum;
import com.kernotec.farm.activity.jpa.service.ConnectionService;
import com.kernotec.farm.activity.rest.ApiSpec.ConnectionSpec;
import com.kernotec.farm.activity.rest.dto.request.connection.ConnectionFindAllFilterRequest;
import com.kernotec.farm.activity.rest.dto.response.connection.ConnectionResponse;
import com.kernotec.farm.activity.rest.mapper.connection.ConnectionResponseMapper;
import com.kernotec.farm.parametric.jpa.enums.RequestStateCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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

@Tag(name = ConnectionSpec.TAG_NAME, description = ConnectionSpec.TAG_DESCRIPTION)
@RequestMapping(path = ConnectionSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class ConnectionController {

    private final ConnectionService connectionService;
    private final ConnectionResponseMapper connectionResponseMapper;

    @Operation(summary = "Find all Connections")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ConnectionResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending,
        @RequestParam(required = false) UUID socialNetworkId,
        @RequestParam(required = false) UUID accountId,
        @RequestParam(required = false) RequestStateCodeEnum requestStateCode,
        @RequestParam(required = false) ConnectionActionEnum action)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<Connection> connectionPage = connectionService.findAllWithFilters(
            ConnectionFindAllFilterRequest.builder()
                .socialNetworkId(socialNetworkId)
                .accountId(accountId)
                .requestStateCode(requestStateCode)
                .action(action)
                .build(), pageable
        );

        return PageResponse.<ConnectionResponse>builder()
            .code(HttpStatus.OK.value())
            .data(connectionResponseMapper.toResponse(connectionPage.getContent()))
            .pagination(PaginationResponse.builder()
                .pages(connectionPage.getTotalPages())
                .count(connectionPage.getTotalElements())
                .build())
            .build();
    }

    @Operation(summary = "Find all Connections unpaginated")
    @GetMapping("unpaginated")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ConnectionResponse> findAllUnpaginated() {
        List<Connection> connectionList = connectionService.findAll();

        return PageResponse.<ConnectionResponse>builder()
            .code(HttpStatus.OK.value())
            .data(connectionResponseMapper.toResponse(connectionList))
            .build();
    }

    @Operation(summary = "Find connection by id")
    @GetMapping("{connectionId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<ConnectionResponse> findById(
        @PathVariable("connectionId") UUID connectionId)
    {
        Connection connection = connectionService.findByIdThrow(connectionId);

        return SingleResponse.<ConnectionResponse>builder()
            .code(HttpStatus.OK.value())
            .data(connectionResponseMapper.toResponse(connection))
            .build();
    }
}
