package com.kernotec.farm.parametric.rest.command.excel;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.parametric.rest.dto.ImportExcelDataDto;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
    AbstractTransactionalRequiredCommand<ExcelImportCmd.Request, Void>
{

    private static final int BATCH_SIZE = 50;

    private final ImportExcelDataGetDtoCmd importExcelDataGetDtoCmd;
    private final ImportExcelRegisterInDbCmd importExcelRegisterInDbCmd;
    private final ImportCsvPersistBatchInDbCmd importCsvPersistBatchInDbCmd;

    @Override
    protected Void run(Request request) {

        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(request.getExcelFile()
            .getInputStream())).withSkipLines(1)
            .build())
        {

            String[] csvRow;

            List<ImportExcelDataDto> importExcelDataDtoBatch = new ArrayList<>(BATCH_SIZE);

            while ((csvRow = reader.readNext()) != null) {
                ImportExcelDataDto importExcelDataDto = importExcelDataGetDtoCmd.withRequest(
                        ImportExcelDataGetDtoCmd.Request.builder()
                            .csvData(csvRow)
                            .build())
                    .execute();

                /*String strJson = JsonUtil.toString(new ObjectMapper(), importExcelDataDto);
                log.info("IMPORT DTO DATA: {}", strJson);*/

                importExcelDataDtoBatch.add(importExcelDataDto);

                if (importExcelDataDtoBatch.size() >= BATCH_SIZE) {
                    addBatchToSave(importExcelDataDtoBatch);
                }
            }

            if (!importExcelDataDtoBatch.isEmpty()) {
                addBatchToSave(importExcelDataDtoBatch);
            }

            throw new RuntimeException("TEST TO NO SAVE DATA");
        } catch (IOException | CsvValidationException ex) {
            log.error("Error loading data in DB: {}", ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

        //return null;
    }

    private void addBatchToSave(List<ImportExcelDataDto> importExcelDataDtoBatch) {
        log.info("Procesing batch of size: {}", importExcelDataDtoBatch.size());
        importCsvPersistBatchInDbCmd.withRequest(ImportCsvPersistBatchInDbCmd.Request.builder()
                .importExcelDataDtoList(new ArrayList<>(importExcelDataDtoBatch))
                .build())
            .execute();

        log.info("Batch of size {} processed SUCCESSFULLY.", importExcelDataDtoBatch.size());
        importExcelDataDtoBatch.clear();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final MultipartFile excelFile;
    }
}
