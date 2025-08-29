package com.kernotec.farm.rest.command.excel;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.jpa.dto.entity.person.PersonDto;
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

    @Override
    protected Void run(Request request) {
        ImportExcelDataDto excelDataDto = request.getImportExcelDataDto();

        PersonDto personRegisterDto = personFindOrCreateCmd.withRequest(
                PersonFindOrCreateCmd.Request.builder()
                    .name(excelDataDto.getPersonRegisterName())
                    .lastName(excelDataDto.getPersonRegisterLastName())
                    .birthDateString(excelDataDto.getBirthDateRegister())
                    .build())
            .execute();

        PersonDto personFakeDto = personFindOrCreateCmd.withRequest(
                PersonFindOrCreateCmd.Request.builder()
                    .name(excelDataDto.getPersonFakeName())
                    .lastName(excelDataDto.getPersonFakeName())
                    .birthDateString(excelDataDto.getBirthDateFake())
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
