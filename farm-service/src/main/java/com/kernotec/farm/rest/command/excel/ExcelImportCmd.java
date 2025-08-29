package com.kernotec.farm.rest.command.excel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.core.util.JsonUtil;
import com.kernotec.farm.rest.dto.ImportExcelDataDto;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jakarta.validation.constraints.NotNull;
import java.io.InputStreamReader;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Slf4j
public class ExcelImportCmd extends
    AbstractTransactionalRequiredCommand<ExcelImportCmd.Request, Void> {

    private final ImportExcelDataGetDtoCmd importExcelDataGetDtoCmd;

    @Override
    protected Void run(Request request) {

        try (CSVReader reader = new CSVReaderBuilder(
            new InputStreamReader(request.getExcelFile().getInputStream())).withSkipLines(1)
            .build()) {

            String[] csvRow;

            while ((csvRow = reader.readNext()) != null) {
                ImportExcelDataDto importExcelDataDto = importExcelDataGetDtoCmd.withRequest(
                    ImportExcelDataGetDtoCmd.Request.builder().csvData(csvRow).build()).execute();

                String strJson = JsonUtil.toString(new ObjectMapper(), importExcelDataDto);
                log.info("IMPORT DTO DATA: {}", strJson);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
