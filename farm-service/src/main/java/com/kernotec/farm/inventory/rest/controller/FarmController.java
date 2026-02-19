package com.kernotec.farm.inventory.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.farm.common.dto.response.MinimalResponse;
import com.kernotec.farm.inventory.jpa.service.FarmService;
import com.kernotec.farm.inventory.rest.ApiSpec.FarmSpec;
import com.kernotec.farm.inventory.rest.dto.response.farm.FarmMinResponse;
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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = FarmSpec.TAG_NAME, description = FarmSpec.TAG_DESCRIPTION)
@RequestMapping(path = FarmSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class FarmController {

    private final FarmService farmService;

    @Operation(summary = "Find minimal all farms data")
    @GetMapping("/minimal")
    public MinimalResponse<List<FarmMinResponse>> findAllMinimal(
        @RequestParam(required = false) String keyword)
    {
        Pageable pageable = PageableUtil.of(0, 30, "createdAt", false);
        Page<FarmMinResponse> farmPage = farmService.findAllMinData(keyword, pageable);

        return MinimalResponse.<List<FarmMinResponse>>builder()
            .code(HttpStatus.OK.value())
            .data(farmPage.getContent())
            .build();
    }
}
