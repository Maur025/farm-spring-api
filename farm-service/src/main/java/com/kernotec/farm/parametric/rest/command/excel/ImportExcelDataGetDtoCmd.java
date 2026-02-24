package com.kernotec.farm.parametric.rest.command.excel;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.parametric.rest.dto.ImportExcelDataDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImportExcelDataGetDtoCmd extends
    AbstractTransactionalRequiredCommand<ImportExcelDataGetDtoCmd.Request, ImportExcelDataDto>
{

    @Override
    protected ImportExcelDataDto run(Request request) {
        String[] csvData = request.getCsvData();

        if (csvData == null) {
            return null;
        }

        ImportExcelDataDto importExcelDataDto = new ImportExcelDataDto();

        importExcelDataDto.setSequentialNumber(getValueOrNull(csvData[0]));
        importExcelDataDto.setDeviceNumber(getValueOrNull(csvData[1]));
        importExcelDataDto.setFarmName(getValueOrNull(csvData[2]));
        importExcelDataDto.setPersonRegisterName(getValueOrNull(csvData[3], true));
        importExcelDataDto.setPersonRegisterLastName(getValueOrNull(csvData[4], true));
        importExcelDataDto.setPhoneNumber(getValueOrNull(csvData[5], true));
        importExcelDataDto.setIsChipInDevice(
            getValueOrNull(csvData[6]) == null ? null : "si".equalsIgnoreCase(csvData[6]));
        importExcelDataDto.setIsHaveWhatsapp(
            getValueOrNull(csvData[7]) == null ? null : "si".equalsIgnoreCase(csvData[7]));

        importExcelDataDto.setEmailUsername(
            "no tiene".equalsIgnoreCase(csvData[8]) ? null : csvData[8]);
        importExcelDataDto.setEmailPassword(
            "no tiene".equalsIgnoreCase(csvData[9]) ? null : csvData[9]);

        importExcelDataDto.setFacebookUsername(getValueOrNull(csvData[10]));
        importExcelDataDto.setFacebookPassword(getValueOrNull(csvData[11]));

        importExcelDataDto.setTiktokUsername(getValueOrNull(csvData[12]));
        importExcelDataDto.setTiktokPassword(getValueOrNull(csvData[13]));

        importExcelDataDto.setXUsername(getValueOrNull(csvData[14]));
        importExcelDataDto.setXPassword(getValueOrNull(csvData[15]));

        importExcelDataDto.setMacAddress(getValueOrNull(csvData[16]));
        importExcelDataDto.setIpAddress(getValueOrNull(csvData[17]));
        importExcelDataDto.setIpGateway(getValueOrNull(csvData[18]));

        importExcelDataDto.setPersonFakeName(getValueOrNull(csvData[19]));
        importExcelDataDto.setPersonFakeLastName(getValueOrNull(csvData[20]));

        importExcelDataDto.setFacebookObservation(getValueOrNull(csvData[21]));
        importExcelDataDto.setTiktokObservation(getValueOrNull(csvData[22]));

        importExcelDataDto.setOperator(getValueOrNull(csvData[23]));

        importExcelDataDto.setImeiOne(getValueOrNull(csvData[24]));
        importExcelDataDto.setImeiTwo(getValueOrNull(csvData[25]));

        importExcelDataDto.setDeviceBrand(getValueOrNull(csvData[26]));
        importExcelDataDto.setDeviceModel(getValueOrNull(csvData[27]));
        importExcelDataDto.setFarmType(getValueOrNull(csvData[28]));
        /*importExcelDataDto.setBirthDateRegister(csvData[29].isBlank() ? null : csvData[29]);
        importExcelDataDto.setBirthDateFake(csvData[30].isBlank() ? null : csvData[30]);*/
        importExcelDataDto.setFacebookAccountLink(getValueOrNull(csvData[31]));

        return importExcelDataDto;
    }

    private String getValueOrNull(String value, boolean ignoreNa) {
        if (value == null || value.isBlank()) {
            return null;
        }

        if (!ignoreNa && value.equalsIgnoreCase("N/A")) {
            return null;
        }

        return value;
    }

    private String getValueOrNull(String value) {
        return getValueOrNull(value, false);
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String[] csvData;
    }
}
