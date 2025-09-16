package com.kernotec.farm.account.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.farm.account.jpa.entity.Person;
import com.kernotec.farm.account.jpa.service.PersonService;
import com.kernotec.farm.account.rest.ApiSpec.PersonSpec;
import com.kernotec.farm.account.rest.dto.response.person.PersonResponse;
import com.kernotec.farm.account.rest.mapper.person.PersonResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final PersonService personService;
    private final PersonResponseMapper personResponseMapper;

    @Operation(summary = "Find all Persons")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<PersonResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<Person> personPage = personService.findAll(pageable);

        return PageResponse.<PersonResponse>builder()
            .code(HttpStatus.OK.value())
            .data(personResponseMapper.toResponse(personPage.getContent()))
            .pagination(PaginationResponse.builder()
                .pages(personPage.getTotalPages())
                .count(personPage.getTotalElements())
                .build())
            .build();
    }
}
