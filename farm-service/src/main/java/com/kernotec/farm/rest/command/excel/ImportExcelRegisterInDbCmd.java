package com.kernotec.farm.rest.command.excel;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.command.assigned.chip.AssignedChipCreateCmd;
import com.kernotec.farm.jpa.dto.entity.chip.ChipDto;
import com.kernotec.farm.jpa.dto.entity.device.DeviceDto;
import com.kernotec.farm.jpa.dto.entity.farm.FarmDto;
import com.kernotec.farm.jpa.dto.entity.person.PersonDto;
import com.kernotec.farm.rest.command.chip.ChipAssignRegisterCmd;
import com.kernotec.farm.rest.command.device.DeviceFindOrCreateCmd;
import com.kernotec.farm.rest.command.farm.FarmFindOrCreateCmd;
import com.kernotec.farm.rest.command.person.PersonFindOrCreateCmd;
import com.kernotec.farm.rest.dto.ImportExcelDataDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImportExcelRegisterInDbCmd extends
    AbstractTransactionalRequiredCommand<ImportExcelRegisterInDbCmd.Request, Void>
{

    private final PersonFindOrCreateCmd personFindOrCreateCmd;
    private final FarmFindOrCreateCmd farmFindOrCreateCmd;
    private final DeviceFindOrCreateCmd deviceFindOrCreateCmd;
    private final ChipAssignRegisterCmd chipAssignRegisterCmd;
    private final AssignedChipCreateCmd assignedChipCreateCmd;

    @Override
    protected Void run(Request request) {
        ImportExcelDataDto excelDataDto = request.getImportExcelDataDto();

        FarmDto farmDto = farmFindOrCreateCmd.withRequest(FarmFindOrCreateCmd.Request.builder()
                .code("FARM_" + excelDataDto.getFarmName())
                .name(excelDataDto.getFarmName())
                .type(excelDataDto.getFarmType())
                .build())
            .execute();

        DeviceDto deviceDto = deviceFindOrCreateCmd.withRequest(
                DeviceFindOrCreateCmd.Request.builder()
                    .deviceNumber(excelDataDto.getDeviceNumber())
                    .name("Dispositivo-" + excelDataDto.getDeviceNumber())
                    .model(excelDataDto.getDeviceModel())
                    .brand(excelDataDto.getDeviceBrand())
                    .farmId(farmDto.getId())
                    .macAddress(excelDataDto.getMacAddress())
                    .ipAddress(excelDataDto.getIpAddress())
                    .ipGateway(excelDataDto.getIpGateway())
                    .imeiOne(excelDataDto.getImeiOne())
                    .imeiTwo(excelDataDto.getImeiTwo())
                    .build())
            .execute();

        PersonDto personRegisterDto = personFindOrCreateCmd.withRequest(
                PersonFindOrCreateCmd.Request.builder()
                    .name(excelDataDto.getPersonRegisterName())
                    .lastName(excelDataDto.getPersonRegisterLastName())
                    .birthDateString(excelDataDto.getBirthDateRegister())
                    .build())
            .execute();

        ChipDto chipDto = chipAssignRegisterCmd.withRequest(ChipAssignRegisterCmd.Request.builder()
                .phoneNumber(excelDataDto.getPhoneNumber())
                .isDeviceInside(excelDataDto.getIsChipInDevice())
                .operatorCode(excelDataDto.getOperator())
                .personId(personRegisterDto.getId())
                .deviceId(deviceDto.getId())
                .build())
            .execute();

        PersonDto personFakeDto = personFindOrCreateCmd.withRequest(
                PersonFindOrCreateCmd.Request.builder()
                    .name(excelDataDto.getPersonFakeName())
                    .lastName(excelDataDto.getPersonFakeName())
                    .birthDateString(excelDataDto.getBirthDateFake())
                    .build())
            .execute();

        assignedChipCreateCmd.withRequest(AssignedChipCreateCmd.Request.builder()
                .chipId(chipDto.getId())
                .personId(personFakeDto.getId())
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final ImportExcelDataDto importExcelDataDto;
    }
}
