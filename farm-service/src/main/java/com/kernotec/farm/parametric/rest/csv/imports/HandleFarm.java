package com.kernotec.farm.parametric.rest.csv.imports;

import com.kernotec.farm.inventory.command.farm.FarmCreateManyCmd;
import com.kernotec.farm.inventory.jpa.entity.Farm;
import com.kernotec.farm.inventory.jpa.enums.FarmTypeEnum;
import com.kernotec.farm.inventory.jpa.service.FarmService;
import com.kernotec.farm.parametric.rest.csv.common.CsvImportCommon;
import com.kernotec.farm.parametric.rest.csv.dto.request.FarmRequest;
import com.kernotec.farm.parametric.rest.csv.enums.HandleTypeEnum;
import com.kernotec.farm.parametric.rest.dto.ImportExcelDataDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HandleFarm implements HandleImport<FarmRequest> {

    private final CsvImportCommon csvImportCommon;
    private final FarmService farmService;
    private final FarmCreateManyCmd farmCreateManyCmd;

    @Override
    public HandleTypeEnum handleType() {
        return HandleTypeEnum.FARM;
    }

    @Override
    public Map<String, UUID> handle(FarmRequest request) {
        Map<String, Farm> farmMemoryMap = request.importExcelDataDtoBatch()
            .stream()
            .collect(Collectors.toMap(
                dto -> csvImportCommon.getFarmCode(dto.getFarmName()), this::getFarmEntity,
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

    private Farm getFarmEntity(ImportExcelDataDto dto) {
        var farmEntity = new Farm();
        farmEntity.setName(dto.getFarmName());
        farmEntity.setCode(csvImportCommon.getFarmCode(dto.getFarmName()));
        farmEntity.setType(FarmTypeEnum.fromValue(dto.getFarmType()));

        return farmEntity;
    }
}
