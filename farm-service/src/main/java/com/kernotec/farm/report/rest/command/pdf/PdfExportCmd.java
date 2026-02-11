package com.kernotec.farm.report.rest.command.pdf;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.report.jpa.enums.ReportDispositionEnum;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PdfExportCmd extends AbstractTransactionalRequiredCommand<PdfExportCmd.Request, Void> {

    @Override
    protected Void run(Request request) {
        HttpServletResponse response = request.response;
        String fileName = Objects.requireNonNullElse(request.fileName, "report")
            .concat(".pdf");

        byte[] reportPdfBytes = request.callbackGetReportBytes.get();

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.builder(
                    String.valueOf(request.disposition))
                .filename(fileName, StandardCharsets.UTF_8)
                .build()
                .toString()
        );

        response.setContentLength(reportPdfBytes.length);

        try (OutputStream out = response.getOutputStream()) {
            out.write(reportPdfBytes);
            out.flush();
        } catch (IOException ex) {
            log.error("Error while exporting PDF report: {}", ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }

        return null;
    }

    @Builder
    public record Request(@NotNull HttpServletResponse response,
                          @NotNull Supplier<byte[]> callbackGetReportBytes,
                          @NotNull ReportDispositionEnum disposition, String fileName)
    {

    }
}
