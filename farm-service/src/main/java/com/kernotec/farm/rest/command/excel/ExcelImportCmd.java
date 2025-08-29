package com.kernotec.farm.rest.command.excel;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import jakarta.validation.constraints.NotNull;
import java.io.File;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ExcelImportCmd extends
    AbstractTransactionalRequiredCommand<ExcelImportCmd.Request, Void>
{

    @Override
    protected Void run(Request request) {
        File file = null;

        try {
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {

        }
        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final MultipartFile excelFile;
    }
}
