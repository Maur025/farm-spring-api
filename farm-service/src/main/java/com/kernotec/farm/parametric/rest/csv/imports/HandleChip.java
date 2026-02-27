package com.kernotec.farm.parametric.rest.csv.imports;

import com.kernotec.farm.inventory.command.chip.ChipCreateManyCmd;
import com.kernotec.farm.inventory.jpa.dto.entity.ChipDto;
import com.kernotec.farm.inventory.jpa.dto.mapper.ChipDtoMapper;
import com.kernotec.farm.inventory.jpa.entity.Chip;
import com.kernotec.farm.parametric.jpa.enums.OperatorCodeEnum;
import com.kernotec.farm.parametric.rest.csv.common.CsvImportCommon;
import com.kernotec.farm.parametric.rest.csv.dto.request.ChipRequest;
import com.kernotec.farm.parametric.rest.csv.enums.HandleTypeEnum;
import com.kernotec.farm.parametric.rest.dto.ImportExcelDataDto;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HandleChip implements HandleImport<ChipRequest> {

    private final CsvImportCommon csvImportCommon;
    private final ChipCreateManyCmd chipCreateManyCmd;
    private final ChipDtoMapper chipDtoMapper;

    @Override
    public HandleTypeEnum handleType() {
        return HandleTypeEnum.CHIP;
    }

    @Override
    public Map<String, UUID> handle(ChipRequest request) {
        List<Chip> chipListToSave = request.importExcelDataDtoBatch()
            .stream()
            .map(dto -> getChipEntity(
                dto, request.operatorMap(), request.registrationPersonMap(),
                request.deviceMap()
            ))
            .toList();

        if (chipListToSave.isEmpty()) {
            return Map.of();
        }

        List<Chip> chipListSaved = chipCreateManyCmd.withRequest(ChipCreateManyCmd.Request.builder()
                .chipList(chipListToSave)
                .build())
            .execute();

        List<ChipDto> chipDtoList = chipDtoMapper.toDto(chipListSaved);

        return chipDtoList.stream()
            .collect(Collectors.toMap(ChipDto::getPhoneNumber, ChipDto::getId));
    }

    private Chip getChipEntity(ImportExcelDataDto dto, Map<OperatorCodeEnum, UUID> operatorMap,
        Map<String, UUID> registrationPersonMap, Map<String, UUID> deviceMap)
    {
        UUID registrationPersonId = null;

        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber()
            .isBlank() && !dto.getPhoneNumber()
            .equalsIgnoreCase("N/A"))

        {
            registrationPersonId = registrationPersonMap.get(
                csvImportCommon.getPersonCode(
                    dto.getPersonRegisterName(), dto.getPersonRegisterLastName()));
        }

        String chipNumber = dto.getPhoneNumber();

        if (registrationPersonId == null) {
            chipNumber = csvImportCommon.getChipUniqueCode(
                dto.getPhoneNumber(), dto.getSequentialNumber());
        }

        var chip = new Chip();

        chip.setPhoneNumber(chipNumber);
        chip.setDeviceInside(
            dto.getIsChipInDevice() == null ? Boolean.FALSE : dto.getIsChipInDevice());
        chip.setOperatorId(dto.getOperator() == null ? null
            : operatorMap.get(OperatorCodeEnum.fromValue(dto.getOperator())));
        chip.setRegistrationPersonId(registrationPersonId);
        chip.setDeviceId(deviceMap.get(dto.getDeviceNumber()));

        return chip;
    }

}
