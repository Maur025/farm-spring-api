package com.kernotec.farm.rest.controller;

import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.farm.rest.ApiSpec.PersonSpec;
import com.kernotec.farm.rest.dto.response.PersonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = PersonSpec.TAG_NAME, description = PersonSpec.TAG_DESCRIPTION)
@RequestMapping(value = PersonSpec.BASE_PATH)
@RestController
@AllArgsConstructor
public class PersonController {

    @Operation(summary = "Find all Persons")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<PersonResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "false") boolean descending,
        @RequestParam(required = false) String keyword)
    {

        return PageResponse.<PersonResponse>builder()
            .code(HttpStatus.OK.value())
            .build();
    }
}
