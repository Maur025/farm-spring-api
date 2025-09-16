package com.kernotec.farm.parametric.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.farm.parametric.jpa.entity.Region;
import com.kernotec.farm.parametric.jpa.service.RegionService;
import com.kernotec.farm.parametric.rest.ApiSpec.RegionSpec;
import com.kernotec.farm.parametric.rest.dto.response.region.RegionResponse;
import com.kernotec.farm.parametric.rest.mapper.region.RegionResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = RegionSpec.TAG_NAME, description = RegionSpec.TAG_DESCRIPTION)
@RequestMapping(path = RegionSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class RegionController {

    private final RegionService regionService;
    private final RegionResponseMapper regionResponseMapper;

    @Operation(summary = "Find all regions")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<RegionResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "false") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<Region> regionPage = regionService.findAll(pageable);

        return PageResponse.<RegionResponse>builder()
            .code(HttpStatus.OK.value())
            .data(regionResponseMapper.toResponse(regionPage.getContent()))
            .pagination(PaginationResponse.builder()
                .pages(regionPage.getTotalPages())
                .count(regionPage.getTotalElements())
                .build())
            .build();
    }

    @Operation(summary = "Find all regions unpaginated")
    @GetMapping("unpaginated")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<RegionResponse> findAllUnpaginated() {
        List<Region> regionList = regionService.findAll();

        return PageResponse.<RegionResponse>builder()
            .code(HttpStatus.OK.value())
            .data(regionResponseMapper.toResponse(regionList))
            .build();
    }
}
