package com.kernotec.farm.parametric.rest.csv.imports;

import com.kernotec.farm.inventory.command.device.DeviceCreateManyCmd;
import com.kernotec.farm.inventory.command.device.connection.DeviceConnectionCreateManyCmd;
import com.kernotec.farm.inventory.command.device.imei.DeviceImeiCreateManyCmd;
import com.kernotec.farm.inventory.jpa.dto.entity.DeviceDto;
import com.kernotec.farm.inventory.jpa.dto.mapper.DeviceDtoMapper;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.jpa.entity.DeviceConnection;
import com.kernotec.farm.inventory.jpa.entity.DeviceImei;
import com.kernotec.farm.inventory.jpa.service.DeviceService;
import com.kernotec.farm.parametric.rest.csv.common.CsvImportCommon;
import com.kernotec.farm.parametric.rest.csv.dto.request.DeviceRequest;
import com.kernotec.farm.parametric.rest.csv.enums.HandleTypeEnum;
import com.kernotec.farm.parametric.rest.dto.ImportExcelDataDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HandleDevice implements HandleImport<DeviceRequest> {

    private final DeviceService deviceService;

    private final DeviceDtoMapper deviceDtoMapper;

    private final CsvImportCommon csvImportCommon;
    private final DeviceCreateManyCmd deviceCreateManyCmd;
    private final DeviceConnectionCreateManyCmd deviceConnectionCreateManyCmd;
    private final DeviceImeiCreateManyCmd deviceImeiCreateManyCmd;

    @Override
    public HandleTypeEnum handleType() {
        return HandleTypeEnum.DEVICE;
    }

    @Override
    public Map<String, UUID> handle(DeviceRequest request) {
        Map<String, Device> deviceMemoryMap = request.importExcelDataDtoBatch()
            .stream()
            .collect(Collectors.toMap(
                ImportExcelDataDto::getDeviceNumber, dto -> getDeviceEntity(dto, request.farmMap()),
                (existing, replacement) -> replacement
            ));

        Set<String> deviceNumberSet = deviceMemoryMap.values()
            .stream()
            .map(Device::getDeviceNumber)
            .collect(Collectors.toSet());

        Page<Device> devicePage = deviceService.findAllByDeviceNumberIn(deviceNumberSet);
        List<DeviceDto> deviceDtoList = deviceDtoMapper.toDto(devicePage.getContent());
        Map<String, UUID> deviceMatchMap = deviceDtoList.stream()
            .collect(Collectors.toMap(DeviceDto::getDeviceNumber, DeviceDto::getId));

        List<Device> deviceListToSave = new ArrayList<>();

        for (Device device : deviceMemoryMap.values()) {
            if (deviceMatchMap.containsKey(device.getDeviceNumber())) {
                continue;
            }

            deviceListToSave.add(device);
        }

        if (deviceListToSave.isEmpty()) {
            return deviceMatchMap;
        }

        Set<String> deviceNumberSetToSave = deviceListToSave.stream()
            .map(Device::getDeviceNumber)
            .collect(Collectors.toSet());

        List<Device> deviceListSaved = deviceCreateManyCmd.withRequest(
                DeviceCreateManyCmd.Request.builder()
                    .deviceList(deviceListToSave)
                    .build())
            .execute();

        for (Device device : deviceListSaved) {
            deviceMatchMap.put(device.getDeviceNumber(), device.getId());
        }

        assignDeviceConnections(
            request.importExcelDataDtoBatch(), deviceMatchMap, deviceNumberSetToSave);
        assignDeviceImeis(request.importExcelDataDtoBatch(), deviceMatchMap, deviceNumberSetToSave);

        return deviceMatchMap;
    }

    private Device getDeviceEntity(ImportExcelDataDto dto, Map<String, UUID> farmMap) {
        var device = new Device();
        device.setName("Dispositivo-" + dto.getDeviceNumber());
        device.setDeviceNumber(dto.getDeviceNumber());
        device.setDeviceNumberLong(Long.valueOf(dto.getDeviceNumber()
            .trim()));
        device.setModel(Objects.requireNonNullElse(dto.getDeviceModel(), "N/A"));
        device.setBrand(Objects.requireNonNullElse(dto.getDeviceBrand(), "N/A"));
        device.setSerialNumber("N/A");
        device.setFarmId(farmMap.get(csvImportCommon.getFarmCode(dto.getFarmName())));

        return device;
    }

    private void assignDeviceConnections(List<ImportExcelDataDto> importExcelDataDtoBatch,
        Map<String, UUID> deviceMap, Set<String> deviceNumberSetToSave)
    {
        Map<String, DeviceConnection> deviceConnectionMemoryMap = importExcelDataDtoBatch.stream()
            .filter(dto -> deviceNumberSetToSave.contains(dto.getDeviceNumber()))
            .collect(Collectors.toMap(
                ImportExcelDataDto::getMacAddress, dto -> getDeviceConnectionEntity(dto, deviceMap),
                (existing, replacement) -> replacement
            ));

        if (deviceConnectionMemoryMap.isEmpty()) {
            return;
        }

        deviceConnectionCreateManyCmd.withRequest(DeviceConnectionCreateManyCmd.Request.builder()
                .deviceConnectionList(deviceConnectionMemoryMap.values()
                    .stream()
                    .toList())
                .build())
            .execute();
    }

    private DeviceConnection getDeviceConnectionEntity(ImportExcelDataDto dto,
        Map<String, UUID> deviceMap)
    {
        var deviceConnection = new DeviceConnection();
        deviceConnection.setMacAddress(Objects.requireNonNullElse(dto.getMacAddress(), "N/A"));
        deviceConnection.setIpAddress(Objects.requireNonNullElse(dto.getIpAddress(), "N/A"));
        deviceConnection.setIpGateway(Objects.requireNonNullElse(dto.getIpGateway(), "N/A"));
        deviceConnection.setNetworkName("N/A");
        deviceConnection.setDeviceId(deviceMap.get(dto.getDeviceNumber()));

        return deviceConnection;
    }

    private void assignDeviceImeis(List<ImportExcelDataDto> importExcelDataDtoBatch,
        Map<String, UUID> deviceMap, Set<String> deviceNumberSetToSave)
    {
        Map<String, DeviceImei> deviceImeiMemoryMap = importExcelDataDtoBatch.stream()
            .filter(dto -> deviceNumberSetToSave.contains(dto.getDeviceNumber()))
            .flatMap(dto -> Stream.of(dto.getImeiOne(), dto.getImeiTwo())
                .filter(Objects::nonNull)
                .map(imei -> Map.entry(imei, dto)))
            .collect(Collectors.toMap(
                Map.Entry::getKey, entry -> getDeviceImeiEntity(
                    entry.getKey(), entry.getValue()
                        .getDeviceNumber(), deviceMap
                ), (existing, replacement) -> existing
            ));

        if (deviceImeiMemoryMap.isEmpty()) {
            return;
        }

        deviceImeiCreateManyCmd.withRequest(DeviceImeiCreateManyCmd.Request.builder()
                .deviceImeiList(deviceImeiMemoryMap.values()
                    .stream()
                    .toList())
                .build())
            .execute();
    }

    private DeviceImei getDeviceImeiEntity(String imei, String deviceNumber,
        Map<String, UUID> deviceMap)
    {
        var deviceImei = new DeviceImei();
        deviceImei.setImei(imei);
        deviceImei.setDeviceId(deviceMap.get(deviceNumber));

        return deviceImei;
    }
}
