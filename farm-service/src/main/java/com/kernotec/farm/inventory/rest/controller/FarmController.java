package com.kernotec.farm.inventory.rest.controller;

import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.farm.inventory.jpa.entity.Farm;
import com.kernotec.farm.inventory.jpa.service.FarmService;
import com.kernotec.farm.inventory.rest.ApiSpec.FarmSpec;
import com.kernotec.farm.inventory.rest.dto.response.farm.FarmResponse;
import com.kernotec.farm.inventory.rest.mapper.farm.FarmResponseMinMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = FarmSpec.TAG_NAME, description = FarmSpec.TAG_DESCRIPTION)
@RequestMapping(path = FarmSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class FarmController {

    private final FarmService farmService;
    private final FarmResponseMinMapper farmResponseMinMapper;

    @Operation(summary = "Find minimal all farms data")
    @GetMapping("/minimal")
    public PageResponse<FarmResponse> findAllMinimal() {
        List<Farm> farmList = farmService.findAll();

        return PageResponse.<FarmResponse>builder()
            .code(HttpStatus.OK.value())
            .data(farmResponseMinMapper.toResponse(farmList))
            .build();
    }
}
