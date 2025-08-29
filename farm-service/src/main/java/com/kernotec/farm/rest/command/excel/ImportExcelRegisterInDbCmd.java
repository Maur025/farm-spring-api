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
public class ImportExcelRegisterInDbCmd extends
    AbstractTransactionalRequiredCommand<ImportExcelRegisterInDbCmd.Request, Void> {

    @Override
    protected Void run(Request request) {
        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final ImportExcelDataDto importExcelDataDto;
    }
}
