package com.kernotec.farm.account.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.farm.account.jpa.entity.Friend;
import com.kernotec.farm.account.jpa.enums.AccountTypeEnum;
import com.kernotec.farm.account.jpa.service.FriendService;
import com.kernotec.farm.account.rest.ApiSpec.FriendSpec;
import com.kernotec.farm.account.rest.command.friend.FriendBreakRelationshipCmd;
import com.kernotec.farm.account.rest.dto.request.friend.FriendBreakRelationshipRequest;
import com.kernotec.farm.account.rest.dto.response.friend.FriendResponse;
import com.kernotec.farm.account.rest.mapper.friend.FriendResponseMapper;
import com.kernotec.farm.parametric.jpa.enums.FriendStateCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = FriendSpec.TAG_NAME, description = FriendSpec.TAG_DESCRIPTION)
@RequestMapping(path = FriendSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class FriendController {

    private final FriendService friendService;
    private final FriendResponseMapper friendResponseMapper;
    private final FriendBreakRelationshipCmd friendBreakRelationshipCmd;

    @Operation(summary = "Find all friends")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<FriendResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending,
        @RequestParam(required = false) UUID accountId,
        @RequestParam(required = false) UUID socialNetworkId,
        @RequestParam(required = false) AccountTypeEnum friendAccountType,
        @RequestParam(required = false) FriendStateCodeEnum friendStateCode)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<Friend> friendPage = friendService.findAllWithFilters(
            accountId, socialNetworkId, friendAccountType, friendStateCode, pageable);

        return PageResponse.<FriendResponse>builder()
            .code(HttpStatus.OK.value())
            .data(friendResponseMapper.toResponse(friendPage.getContent()))
            .pagination(PaginationResponse.builder()
                .pages(friendPage.getTotalPages())
                .count(friendPage.getTotalElements())
                .build())
            .build();
    }

    @Operation(summary = "Find friend by id")
    @GetMapping("{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<FriendResponse> findById(@PathVariable UUID friendId) {
        Friend friend = friendService.findByIdThrow(friendId);

        return SingleResponse.<FriendResponse>builder()
            .code(HttpStatus.OK.value())
            .data(friendResponseMapper.toResponse(friend))
            .build();
    }

    @Operation(summary = "Break friendship by id")
    @PostMapping("{friendId}/break-relationship")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<FriendResponse> breakRelationship(@PathVariable UUID friendId,
        @RequestBody FriendBreakRelationshipRequest request)
    {
        friendBreakRelationshipCmd.withRequest(FriendBreakRelationshipCmd.Request.builder()
                .friendId(friendId)
                .breakRequest(request)
                .build())
            .execute();

        return SingleResponse.<FriendResponse>builder()
            .code(HttpStatus.OK.value())
            .message("Friendship broken successfully")
            .build();
    }
}
