package com.kernotec.farm.report.rest.command.pdf;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.report.jpa.enums.ReportDispositionEnum;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Supplier;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PdfExportCmd extends AbstractTransactionalRequiredCommand<PdfExportCmd.Request, Void> {

    @Override
    protected Void run(Request request) {
        HttpServletResponse response = request.response();

        try (OutputStream out = response.getOutputStream()) {
            byte[] reportPdfBytes = request.callbackGetReportBytes()
                .get();

            response.setContentType("application/pdf");
            response.setHeader(
                "Content-Disposition",
                String.format("%s; filename=\"%s\"", request.disposition(), request.fileName())
            );

            response.setContentLength(reportPdfBytes.length);

            out.write(reportPdfBytes);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Builder
    public record Request(HttpServletResponse response, Supplier<byte[]> callbackGetReportBytes,
                          ReportDispositionEnum disposition, String fileName)
    {

    }
}
