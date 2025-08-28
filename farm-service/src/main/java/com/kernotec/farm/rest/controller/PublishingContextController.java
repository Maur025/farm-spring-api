package com.kernotec.farm.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.farm.jpa.entity.PublishingContext;
import com.kernotec.farm.jpa.service.PublishingContextService;
import com.kernotec.farm.rest.ApiSpec.PublishingContextSpec;
import com.kernotec.farm.rest.dto.response.publishing.context.PublishingContextResponse;
import com.kernotec.farm.rest.mapper.publishing.context.PublishingContextResponseMapper;
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

@Tag(name = PublishingContextSpec.TAG_NAME, description = PublishingContextSpec.TAG_DESCRIPTION)
@RequestMapping(path = PublishingContextSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class PublishingContextController {

    private final PublishingContextService publishingContextService;
    private final PublishingContextResponseMapper publishingContextResponseMapper;

    @Operation(summary = "Find all Publishing Contexts")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<PublishingContextResponse> findAll(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "false") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<PublishingContext> publishingContextPage = publishingContextService.findAll(pageable);

        return PageResponse.<PublishingContextResponse>builder()
            .code(HttpStatus.OK.value())
            .data(publishingContextResponseMapper.toResponse(publishingContextPage.getContent()))
            .pagination(PaginationResponse.builder()
                .pages(publishingContextPage.getTotalPages())
                .count(publishingContextPage.getTotalElements())
                .build())
            .build();
    }

    @Operation(summary = "Find all publishing contexts unpaginated")
    @GetMapping("unpaginated")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<PublishingContextResponse> findAllUnpaginated() {
        List<PublishingContext> publishingContextList = publishingContextService.findAll();

        return PageResponse.<PublishingContextResponse>builder()
            .code(HttpStatus.OK.value())
            .data(publishingContextResponseMapper.toResponse(publishingContextList))
            .build();
    }

    @Operation(summary = "Find publishing contexts by id")
    @GetMapping("{publishingContextId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<PublishingContextResponse> findById(
        @PathVariable("publishingContextId") UUID publishingContextId)
    {
        PublishingContext publishingContext = publishingContextService.findByIdThrow(
            publishingContextId);

        return SingleResponse.<PublishingContextResponse>builder()
            .code(HttpStatus.OK.value())
            .data(publishingContextResponseMapper.toResponse(publishingContext))
            .build();
    }
}
