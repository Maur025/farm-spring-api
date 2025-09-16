package com.kernotec.farmauth.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.farmauth.command.UserCreateCmd;
import com.kernotec.farmauth.jpa.entity.User;
import com.kernotec.farmauth.jpa.service.UserService;
import com.kernotec.farmauth.rest.ApiSpec.UserSpec;
import com.kernotec.farmauth.rest.dto.request.UserCreateRequest;
import com.kernotec.farmauth.rest.dto.response.UserResponse;
import com.kernotec.farmauth.rest.mapper.UserResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = UserSpec.TAG_NAME, description = UserSpec.TAG_DESCRIPTION)
@RequestMapping(path = UserSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserResponseMapper userResponseMapper;
    private final UserCreateCmd userCreateCmd;

    @Operation(summary = "Find all users")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<UserResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdOn") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<User> userPage = userService.findAll(pageable);

        return PageResponse.<UserResponse>builder()
            .code(HttpStatus.OK.value())
            .data(userResponseMapper.toResponse(userPage.getContent()))
            .pagination(PaginationResponse.builder()
                .pages(userPage.getTotalPages())
                .count(userPage.getTotalElements())
                .build())
            .build();
    }

    @Operation(summary = "Create user")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResponse<UserResponse> create(@RequestBody UserCreateRequest request) {
        UUID userId = userCreateCmd.withRequest(UserCreateCmd.Request.builder()
                .name(request.getName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(request.getPassword())
                .build())
            .execute();

        return SingleResponse.<UserResponse>builder()
            .code(HttpStatus.CREATED.value())
            .data(userResponseMapper.toResponse(userId))
            .message("User created successfully")
            .build();
    }
}
