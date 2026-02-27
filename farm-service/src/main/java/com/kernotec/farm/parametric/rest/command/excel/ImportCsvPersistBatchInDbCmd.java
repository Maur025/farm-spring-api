package com.kernotec.farm.parametric.rest.command.excel;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.core.jpa.util.PageableUtil;
import com.kernotec.farm.parametric.jpa.dto.entity.SocialNetworkDto;
import com.kernotec.farm.parametric.jpa.dto.mapper.SocialNetworkDtoFlatMapper;
import com.kernotec.farm.parametric.jpa.entity.Operator;
import com.kernotec.farm.parametric.jpa.entity.SocialNetwork;
import com.kernotec.farm.parametric.jpa.enums.OperatorCodeEnum;
import com.kernotec.farm.parametric.jpa.service.OperatorService;
import com.kernotec.farm.parametric.jpa.service.SocialNetworkService;
import com.kernotec.farm.parametric.rest.csv.dto.request.AccountRequest;
import com.kernotec.farm.parametric.rest.csv.dto.request.AssignAccountRequest;
import com.kernotec.farm.parametric.rest.csv.dto.request.ChipRequest;
import com.kernotec.farm.parametric.rest.csv.dto.request.DeviceRequest;
import com.kernotec.farm.parametric.rest.csv.dto.request.FarmRequest;
import com.kernotec.farm.parametric.rest.csv.dto.request.PersonRegisterRequest;
import com.kernotec.farm.parametric.rest.csv.dto.request.PersonRequest;
import com.kernotec.farm.parametric.rest.csv.enums.HandleTypeEnum;
import com.kernotec.farm.parametric.rest.csv.imports.HandleImport;
import com.kernotec.farm.parametric.rest.csv.imports.HandleImportFactory;
import com.kernotec.farm.parametric.rest.dto.ImportExcelDataDto;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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

    private final OperatorService operatorService;
    private final HandleImportFactory handleImportFactory;
    private final SocialNetworkService socialNetworkService;
    private final SocialNetworkDtoFlatMapper socialNetworkDtoFlatMapper;

    @Override
    protected Void run(Request request) {
        if (request.importExcelDataDtoBatch.isEmpty()) {
            return null;
        }

        HandleImport<FarmRequest> handleFarm = handleImportFactory.getHandler(HandleTypeEnum.FARM);
        Map<String, UUID> farmMap = handleFarm.handle(FarmRequest.builder()
            .importExcelDataDtoBatch(request.importExcelDataDtoBatch)
            .build());

        HandleImport<DeviceRequest> handleDevice = handleImportFactory.getHandler(
            HandleTypeEnum.DEVICE);

        Map<String, UUID> deviceMap = handleDevice.handle(DeviceRequest.builder()
            .importExcelDataDtoBatch(request.importExcelDataDtoBatch)
            .farmMap(farmMap)
            .build());

        HandleImport<PersonRequest> handlePerson = handleImportFactory.getHandler(
            HandleTypeEnum.PERSON);

        Map<String, UUID> personMap = handlePerson.handle(PersonRequest.builder()
            .importExcelDataDtoBatch(request.importExcelDataDtoBatch)
            .build());

        HandleImport<PersonRegisterRequest> handlePersonRegister = handleImportFactory.getHandler(
            HandleTypeEnum.PERSON_REGISTER);

        Map<String, UUID> personRegisterMap = handlePersonRegister.handle(
            PersonRegisterRequest.builder()
                .importExcelDataDtoBatch(request.importExcelDataDtoBatch)
                .build());

        Map<OperatorCodeEnum, UUID> operatorMap = getOperatorMap(request.importExcelDataDtoBatch);

        HandleImport<ChipRequest> handleChip = handleImportFactory.getHandler(HandleTypeEnum.CHIP);

        Map<String, UUID> chipMap = handleChip.handle(ChipRequest.builder()
            .importExcelDataDtoBatch(request.importExcelDataDtoBatch)
            .operatorMap(operatorMap)
            .registrationPersonMap(personRegisterMap)
            .deviceMap(deviceMap)
            .build());

        Map<String, UUID> socialNetworkMap = getSocialNetworkMap();

        Map<UUID, String> inverseSocialNetworkMap = socialNetworkMap.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        HandleImport<AccountRequest> handleAccount = handleImportFactory.getHandler(
            HandleTypeEnum.ACCOUNT);

        Map<String, UUID> accountMap = handleAccount.handle(AccountRequest.builder()
            .importExcelDataDtoBatch(request.importExcelDataDtoBatch)
            .personMap(personMap)
            .socialNetworkMap(socialNetworkMap)
            .inverseSocialNetworkMap(inverseSocialNetworkMap)
            .build());

        HandleImport<AssignAccountRequest> handleAssignAccount = handleImportFactory.getHandler(
            HandleTypeEnum.ASSIGN_ACCOUNT);

        handleAssignAccount.handle(AssignAccountRequest.builder()
            .importExcelDataDtoBatch(request.importExcelDataDtoBatch)
            .accountMap(accountMap)
            .deviceMap(deviceMap)
            .chipMap(chipMap)
            .build());

        return null;
    }

    private Map<OperatorCodeEnum, UUID> getOperatorMap(
        List<ImportExcelDataDto> importExcelDataDtoBatch)
    {
        Set<OperatorCodeEnum> operatorCodeSet = importExcelDataDtoBatch.stream()
            .map(dto -> OperatorCodeEnum.fromValue(dto.getOperator()))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        if (operatorCodeSet.isEmpty()) {
            return Map.of();
        }

        return operatorService.findAllByCodeIn(operatorCodeSet)
            .getContent()
            .stream()
            .collect(Collectors.toMap(Operator::getCode, Operator::getId));
    }

    private Map<String, UUID> getSocialNetworkMap() {
        Page<SocialNetwork> socialNetworkPage = socialNetworkService.findAll(
            PageableUtil.of(0, 20, "name", false));

        List<SocialNetworkDto> socialNetworkDtoList = socialNetworkDtoFlatMapper.toDto(
            socialNetworkPage.getContent());

        return socialNetworkDtoList.stream()
            .collect(Collectors.toMap(SocialNetworkDto::getCode, SocialNetworkDto::getId));
    }

    @Builder
    public record Request(@NotNull List<ImportExcelDataDto> importExcelDataDtoBatch) {

    }
}
