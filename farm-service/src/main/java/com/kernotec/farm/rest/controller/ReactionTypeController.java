package com.kernotec.farm.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.farm.parametric.jpa.entity.ReactionType;
import com.kernotec.farm.parametric.jpa.service.ReactionTypeService;
import com.kernotec.farm.rest.ApiSpec.ReactionTypeSpec;
import com.kernotec.farm.rest.dto.response.reaction.type.ReactionTypeResponse;
import com.kernotec.farm.rest.mapper.reaction.type.ReactionTypeResponseMapper;
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

@Tag(name = ReactionTypeSpec.TAG_NAME, description = ReactionTypeSpec.TAG_DESCRIPTION)
@RequestMapping(path = ReactionTypeSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class ReactionTypeController {

    private final ReactionTypeService reactionTypeService;
    private final ReactionTypeResponseMapper reactionTypeResponseMapper;

    @Operation(summary = "Find all Reaction Types")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ReactionTypeResponse> findAll(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "false") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<ReactionType> reactionTypePage = reactionTypeService.findAll(pageable);

        return PageResponse.<ReactionTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(reactionTypeResponseMapper.toResponse(reactionTypePage.getContent()))
            .pagination(PaginationResponse.builder()
                .pages(reactionTypePage.getTotalPages())
                .count(reactionTypePage.getTotalElements())
                .build())
            .build();
    }

    @Operation(summary = "Find all reaction types unpaginated")
    @GetMapping("unpaginated")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ReactionTypeResponse> findAllUnpaginated(
        @RequestParam(required = false) UUID socialNetworkId)
    {
        List<ReactionType> reactionTypeList = reactionTypeService.findAllWithFilters(
            socialNetworkId);

        return PageResponse.<ReactionTypeResponse>builder()
            .code(HttpStatus.OK.value())
            .data(reactionTypeResponseMapper.toResponse(reactionTypeList))
            .build();
    }
}
