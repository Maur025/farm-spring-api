package com.kernotec.farm.parametric.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.farm.parametric.jpa.entity.SocialNetwork;
import com.kernotec.farm.parametric.jpa.service.SocialNetworkService;
import com.kernotec.farm.parametric.rest.ApiSpec.SocialNetworkSpec;
import com.kernotec.farm.parametric.rest.dto.response.social.network.SocialNetworkResponse;
import com.kernotec.farm.parametric.rest.mapper.social.network.SocialNetworkResponseMapper;
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

@Tag(name = SocialNetworkSpec.TAG_NAME, description = SocialNetworkSpec.TAG_DESCRIPTION)
@RequestMapping(path = SocialNetworkSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class SocialNetworkController {

    private final SocialNetworkService socialNetworkService;
    private final SocialNetworkResponseMapper socialNetworkResponseMapper;

    @Operation(summary = "Find all Social Networks")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<SocialNetworkResponse> findAll(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "false") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<SocialNetwork> socialNetworkPage = socialNetworkService.findAll(pageable);

        return PageResponse.<SocialNetworkResponse>builder()
            .code(HttpStatus.OK.value())
            .data(socialNetworkResponseMapper.toResponse(socialNetworkPage.getContent()))
            .pagination(PaginationResponse.builder()
                .pages(socialNetworkPage.getTotalPages())
                .count(socialNetworkPage.getTotalElements())
                .build())
            .build();
    }

    @Operation(summary = "Find all Social Networks unpaginated")
    @GetMapping("unpaginated")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<SocialNetworkResponse> findAllUnpaginated(
        @RequestParam(required = false) Boolean isHasAccounts)
    {
        List<SocialNetwork> socialNetworkList = socialNetworkService.findAllWithFilters(
            isHasAccounts);

        return PageResponse.<SocialNetworkResponse>builder()
            .code(HttpStatus.OK.value())
            .data(socialNetworkResponseMapper.toResponse(socialNetworkList))
            .build();
    }

    @Operation(summary = "Find Social Networks by Id")
    @GetMapping("{socialNetworkId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<SocialNetworkResponse> findById(
        @PathVariable("socialNetworkId") UUID socialNetworkId)
    {
        SocialNetwork socialNetwork = socialNetworkService.findByIdThrow(socialNetworkId);

        return SingleResponse.<SocialNetworkResponse>builder()
            .code(HttpStatus.OK.value())
            .data(socialNetworkResponseMapper.toResponse(socialNetwork))
            .build();
    }
}
