package com.kernotec.farm.report.rest.command.excel;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.util.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExcelExportCmd extends
    AbstractTransactionalRequiredCommand<ExcelExportCmd.Request, Void>
{

    private final static String MEDIA_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private final ExcelUtil excelUtil;

    @Override
    protected Void run(Request request) {
        File tempFile = excelUtil.createTempFile("report-", ".xlsx");

        processWorkbook(request.callback, tempFile);

        writeWorkbookToResponse(request.fileName, request.response, tempFile);

        return null;
    }

    private void processWorkbook(Consumer<SXSSFWorkbook> callback, File tempFile) {
        try (var workbook = new SXSSFWorkbook(500)) {
            callback.accept(workbook);

            try (var fileOutputStream = new FileOutputStream(tempFile)) {
                workbook.write(fileOutputStream);
            }
        } catch (IOException ex) {
            log.error("Error creating temporary file: {}", ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    private void writeWorkbookToResponse(String fileNameReq, HttpServletResponse response,
        File tempFile)
    {
        String fileName = Objects.requireNonNullElse(fileNameReq, "report")
            .concat(".xlsx");

        response.setContentType(MEDIA_TYPE_XLSX);
        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                .filename(fileName, StandardCharsets.UTF_8)
                .build()
                .toString()
        );

        response.setContentLength((int) tempFile.length());

        try (InputStream in = new FileInputStream(
            tempFile); OutputStream out = response.getOutputStream())
        {
            in.transferTo(out);
            out.flush();
        } catch (IOException ex) {
            log.error("Error writing Excel to response: {}", ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } finally {
            deleteTempFile(tempFile);
        }
    }

    private void deleteTempFile(File tempFile) {
        try {
            Files.deleteIfExists(tempFile.toPath());
            log.debug("Temporary file deleted: {}", tempFile.getAbsolutePath());
        } catch (IOException ex) {
            log.warn("Could not delete temporary file: {}", ex.getMessage(), ex);
        }
    }

    @Builder
    public record Request(@NotNull Consumer<SXSSFWorkbook> callback,
                          @NotNull HttpServletResponse response, String reportTitle,
                          String fileName)
    {

    }
}
