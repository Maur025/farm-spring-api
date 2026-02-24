package com.kernotec.farm.parametric.rest.command.excel;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.Person;
import com.kernotec.farm.inventory.command.device.DeviceCreateManyCmd;
import com.kernotec.farm.inventory.command.device.connection.DeviceConnectionCreateManyCmd;
import com.kernotec.farm.inventory.command.device.imei.DeviceImeiCreateManyCmd;
import com.kernotec.farm.inventory.command.farm.FarmCreateManyCmd;
import com.kernotec.farm.inventory.jpa.dto.entity.DeviceDto;
import com.kernotec.farm.inventory.jpa.dto.mapper.DeviceDtoMapper;
import com.kernotec.farm.inventory.jpa.entity.Device;
import com.kernotec.farm.inventory.jpa.entity.DeviceConnection;
import com.kernotec.farm.inventory.jpa.entity.DeviceImei;
import com.kernotec.farm.inventory.jpa.entity.Farm;
import com.kernotec.farm.inventory.jpa.enums.FarmTypeEnum;
import com.kernotec.farm.inventory.jpa.service.DeviceService;
import com.kernotec.farm.inventory.jpa.service.FarmService;
import com.kernotec.farm.parametric.rest.dto.ImportExcelDataDto;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImportCsvPersistBatchInDbCmd extends
    AbstractTransactionalRequiredCommand<ImportCsvPersistBatchInDbCmd.Request, Void>
{

    private final FarmService farmService;
    private final FarmCreateManyCmd farmCreateManyCmd;
    private final DeviceService deviceService;
    private final DeviceDtoMapper deviceDtoMapper;
    private final DeviceCreateManyCmd deviceCreateManyCmd;
    private final DeviceConnectionCreateManyCmd deviceConnectionCreateManyCmd;
    private final DeviceImeiCreateManyCmd deviceImeiCreateManyCmd;

    @Override
    protected Void run(Request request) {
        if (request.importExcelDataDtoList.isEmpty()) {
            return null;
        }

        Map<String, UUID> farmMap = getFarmMap(request.importExcelDataDtoList);

        Map<String, UUID> deviceMap = getDeviceMap(request.importExcelDataDtoList, farmMap);

        Map<String, UUID> personMap = getPersonMap(request.importExcelDataDtoList);
/*
        log.info("farm map size: {}", farmMap.size());

        log.info("device map size: {}", deviceMap.size());*/

        log.info("REGISTRO EL LOTE EXITOSAMENTE");

        return null;
    }

    private Map<String, UUID> getPersonMap(List<ImportExcelDataDto> importExcelDataDtoBatch) {
        Map<String, Person> personMemoryMap = importExcelDataDtoBatch.stream()
            .collect(Collectors.toMap(
                dto -> String.format(
                    "%s_%s", dto.getPersonRegisterName(), dto.getPersonRegisterLastName()),
                dto -> getPersonEntity(
                    dto.getPersonRegisterName(),
                    dto.getPersonRegisterLastName()
                ), (existing, replacement) -> replacement
            ));

        return Map.of();
    }

    private Person getPersonEntity(String personName, String personLastName) {
        var person = new Person();

        person.setName(personName);
        person.setLastName(personLastName);

        return person;
    }

    private Map<String, UUID> getDeviceMap(List<ImportExcelDataDto> importExcelDataDtoBatch,
        Map<String, UUID> farmMap)
    {
        Map<String, Device> deviceMemoryMap = importExcelDataDtoBatch.stream()
            .collect(
                Collectors.toMap(
                    ImportExcelDataDto::getDeviceNumber, dto -> getDeviceEntity(dto, farmMap),
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

        log.info("device list to save: {}", deviceListToSave.size());

        if (!deviceListToSave.isEmpty()) {
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

            assignDeviceConnections(importExcelDataDtoBatch, deviceMatchMap, deviceNumberSetToSave);
            assignDeviceImeis(importExcelDataDtoBatch, deviceMatchMap, deviceNumberSetToSave);
        }

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
        device.setFarmId(farmMap.get(getFarmCode(dto.getFarmName())));

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

        log.info("device imei one to save: {}", deviceImeiMemoryMap.size());
    }

    private DeviceImei getDeviceImeiEntity(String imei, String deviceNumber,
        Map<String, UUID> deviceMap)
    {
        var deviceImei = new DeviceImei();
        deviceImei.setImei(imei);
        deviceImei.setDeviceId(deviceMap.get(deviceNumber));

        return deviceImei;
    }

    private Map<String, UUID> getFarmMap(List<ImportExcelDataDto> importExcelDataDtoBatch) {
        Map<String, Farm> farmMemoryMap = importExcelDataDtoBatch.stream()
            .collect(
                Collectors.toMap(
                    dto -> getFarmCode(dto.getFarmName()), this::getFarmEntity,
                    (existing, replacement) -> replacement
                ));

        Set<String> farmCodeSet = farmMemoryMap.values()
            .stream()
            .map(Farm::getCode)
            .collect(Collectors.toSet());

        Map<String, UUID> farmMatchMap = farmService.findAllByCodeIn(farmCodeSet)
            .getContent()
            .stream()
            .collect(Collectors.toMap(Farm::getCode, Farm::getId));

        List<Farm> farmListToSave = new ArrayList<>();

        for (Farm farm : farmMemoryMap.values()) {
            if (farmMatchMap.containsKey(farm.getCode())) {
                continue;
            }

            farmListToSave.add(farm);
        }

        if (!farmListToSave.isEmpty()) {
            List<Farm> farmListSaved = farmCreateManyCmd.withRequest(
                    FarmCreateManyCmd.Request.builder()
                        .farmList(farmListToSave)
                        .build())
                .execute();

            for (Farm farm : farmListSaved) {
                farmMatchMap.put(farm.getCode(), farm.getId());
            }
        }

        return farmMatchMap;
    }

    private String getFarmCode(String name) {
        return "FARM_" + name;
    }

    private Farm getFarmEntity(ImportExcelDataDto dto) {
        var farmEntity = new Farm();
        farmEntity.setName(dto.getFarmName());
        farmEntity.setCode(getFarmCode(dto.getFarmName()));
        farmEntity.setType(FarmTypeEnum.fromValue(dto.getFarmType()));

        return farmEntity;
    }

    @Builder
    public record Request(@NotNull List<ImportExcelDataDto> importExcelDataDtoList) {

    }
}
