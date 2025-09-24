package com.kernotec.farm.inventory.rest.controller;

import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.PaginationResponse;
import com.kernotec.core.rest.dto.response.SingleResponse;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.jpa.service.DeviceService;
import com.kernotec.farm.inventory.rest.ApiSpec.DeviceSpec;
import com.kernotec.farm.inventory.rest.dto.response.device.DeviceResponse;
import com.kernotec.farm.inventory.rest.mapper.device.DeviceResponseFilterMapper;
import com.kernotec.farm.inventory.rest.mapper.device.DeviceResponseMapper;
import com.kernotec.farm.inventory.rest.mapper.device.DeviceResponseMinMapper;
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

@Tag(name = DeviceSpec.TAG_NAME, description = DeviceSpec.TAG_DESCRIPTION)
@RequestMapping(path = DeviceSpec.BASE_PATH)
@AllArgsConstructor
@RestController
public class DeviceController {

    private final DeviceService deviceService;
    private final DeviceResponseMapper deviceResponseMapper;
    private final DeviceResponseFilterMapper deviceResponseFilterMapper;
    private final DeviceResponseMinMapper deviceResponseMinMapper;

    @Operation(summary = "Find all Devices")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<DeviceResponse> findAll(@RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "true") boolean descending,
        @RequestParam(required = false) UUID socialNetworkId,
        @RequestParam(required = false) String keyword)
    {
        Pageable pageable = PageableUtil.of(page, size, sortBy, descending);
        Page<Device> devicePage = deviceService.findAllWithFilters(
            keyword, socialNetworkId, pageable);

        return PageResponse.<DeviceResponse>builder()
            .code(HttpStatus.OK.value())
            .data(deviceResponseFilterMapper.toResponse(
                devicePage.getContent(), socialNetworkId,
                keyword
            ))
            .pagination(PaginationResponse.builder()
                .pages(devicePage.getTotalPages())
                .count(devicePage.getTotalElements())
                .build())
            .build();
    }

    @Operation(summary = "Find all Devices unpaginated")
    @GetMapping("unpaginated")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<DeviceResponse> findAllUnpaginated() {
        List<Device> deviceList = deviceService.findAll();

        return PageResponse.<DeviceResponse>builder()
            .code(HttpStatus.OK.value())
            .data(deviceResponseMapper.toResponse(deviceList))
            .build();
    }

    @Operation(summary = "Find all devices with minimal data")
    @GetMapping("minimal")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<DeviceResponse> findAllMinimal(@RequestParam(required = false) UUID farmId)
    {
        List<Device> deviceList = deviceService.findAllWithMinData(farmId);

        return PageResponse.<DeviceResponse>builder()
            .code(HttpStatus.OK.value())
            .data(deviceResponseMinMapper.toResponse(deviceList))
            .build();
    }

    @Operation(summary = "Find device by id")
    @GetMapping("{deviceId}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResponse<DeviceResponse> findById(@PathVariable("deviceId") UUID deviceId) {
        Device device = deviceService.findByIdThrow(deviceId);

        return SingleResponse.<DeviceResponse>builder()
            .code(HttpStatus.OK.value())
            .data(deviceResponseMapper.toResponse(device))
            .build();
    }
}
