package com.kernotec.farm.report.rest.command.excel;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExcelExportCmd extends
    AbstractTransactionalRequiredCommand<ExcelExportCmd.Request, Void>
{

    @Override
    protected Void run(Request request) {
        try (var workbook = new SXSSFWorkbook(500); OutputStream out = request.response()
            .getOutputStream())
        {
            Sheet sheet = workbook.createSheet(
                request.reportTitle() == null ? "Report" : request.reportTitle());

            request.callback()
                .accept(sheet);

            workbook.write(out);
            out.flush();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return null;
    }

    @Builder
    public record Request(@NotNull Consumer<Sheet> callback, @NotNull HttpServletResponse response,
                          String reportTitle)
    {

    }
}
