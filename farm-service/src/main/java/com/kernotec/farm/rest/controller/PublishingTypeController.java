package com.kernotec.farm.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.farm.parametric.jpa.entity.PublishingType;
import com.kernotec.farm.parametric.jpa.service.PublishingTypeService;
import com.kernotec.farm.rest.ApiSpec.PublishingTypeSpec;
import com.kernotec.farm.rest.dto.response.publishing.type.PublishingTypeResponse;
import com.kernotec.farm.rest.mapper.publishing.type.PublishingTypeResponseMapper;
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

@Tag(name = PublishingTypeSpec.TAG_NAME, description = PublishingTypeSpec.TAG_DESCRIPTION)
@RequestMapping(path = PublishingTypeSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class PublishingTypeController {

    private final PublishingTypeService publishingTypeService;
    private final PublishingTypeResponseMapper publishingTypeResponseMapper;

    @Operation(summary = "Find All Publishing Types")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<PublishingTypeResponse> findAll(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "false") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<PublishingType> publishingTypePage = publishingTypeService.findAll(pageable);

        return PageResponse.<PublishingTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(publishingTypeResponseMapper.toResponse(publishingTypePage.getContent()))
            .pagination(PaginationResponse.builder()
                .pages(publishingTypePage.getTotalPages())
                .count(publishingTypePage.getTotalElements())
                .build())
            .build();
    }

    @Operation(summary = "Find all publishing types unpaginated")
    @GetMapping("unpaginated")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<PublishingTypeResponse> findAllUnpaginated() {
        List<PublishingType> publishingTypeList = publishingTypeService.findAll();

        return PageResponse.<PublishingTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(publishingTypeResponseMapper.toResponse(publishingTypeList))
            .build();
    }

    @Operation(summary = "Find publishing types by id")
    @GetMapping("{publishingTypeId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<PublishingTypeResponse> findById(
        @PathVariable("publishingTypeId") UUID publishingTypeId)
    {
        PublishingType publishingType = publishingTypeService.findByIdThrow(publishingTypeId);

        return SingleResponse.<PublishingTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(publishingTypeResponseMapper.toResponse(publishingType))
            .build();
    }
}