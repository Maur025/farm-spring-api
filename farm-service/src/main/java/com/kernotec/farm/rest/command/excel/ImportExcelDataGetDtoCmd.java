package com.kernotec.farm.rest.command.excel;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.rest.dto.ImportExcelDataDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImportExcelDataGetDtoCmd extends
    AbstractTransactionalRequiredCommand<ImportExcelDataGetDtoCmd.Request, ImportExcelDataDto> {

    @Override
    protected ImportExcelDataDto run(Request request) {
        String[] csvData = request.getCsvData();

        if (csvData == null) {
            return null;
        }

        ImportExcelDataDto importExcelDataDto = new ImportExcelDataDto();

        importExcelDataDto.setSequentialNumber(csvData[0]);
        importExcelDataDto.setDeviceNumber(csvData[1]);
        importExcelDataDto.setFarmName(csvData[2]);
        importExcelDataDto.setPersonRegisterName(csvData[3]);
        importExcelDataDto.setPersonRegisterLastName(csvData[4].isBlank() ? null : csvData[4]);
        importExcelDataDto.setPhoneNumber(csvData[5]);
        importExcelDataDto.setIsChipInDevice("si".equalsIgnoreCase(csvData[6]));
        importExcelDataDto.setIsHaveWhatsapp("si".equalsIgnoreCase(csvData[7]));
        importExcelDataDto.setEmailUsername(
            "no tiene".equalsIgnoreCase(csvData[8]) ? null : csvData[8]);
        importExcelDataDto.setEmailPassword(
            "no tiene".equalsIgnoreCase(csvData[9]) ? null : csvData[9]);
        importExcelDataDto.setFacebookUsername(csvData[10]);
        importExcelDataDto.setFacebookPassword(csvData[11]);
        importExcelDataDto.setTiktokUsername(csvData[12]);
        importExcelDataDto.setTiktokPassword(csvData[13]);
        importExcelDataDto.setXUsername(csvData[14].isBlank() ? null : csvData[14]);
        importExcelDataDto.setXPassword(csvData[15].isBlank() ? null : csvData[15]);
        importExcelDataDto.setMacAddress(csvData[16].isBlank() ? null : csvData[16]);
        importExcelDataDto.setIpAddress(csvData[17].isBlank() ? null : csvData[17]);
        importExcelDataDto.setIpGateway(csvData[18].isBlank() ? null : csvData[18]);
        importExcelDataDto.setPersonFakeName(csvData[19]);
        importExcelDataDto.setPersonFakeLastName(csvData[20]);

        return importExcelDataDto;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String[] csvData;
    }
}
