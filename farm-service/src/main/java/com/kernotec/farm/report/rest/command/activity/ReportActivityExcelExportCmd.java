package com.kernotec.farm.report.rest.command.activity;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportActivityExcelExportCmd extends
    AbstractTransactionalRequiredCommand<ReportActivityExcelExportCmd.Request, Void>
{

    @Override
    protected Void run(Request request) {
      /*  try (var workbook = new SXSSFWorkbook(100)) {
            SXSSFSheet sheet = workbook.createSheet("Report-Activity");

            for (int i = 0; i < 10; i++) {
                Row row = sheet.createRow(i);
                row.createCell(0)
                    .setCellValue("Data " + i);
                row.createCell(1)
                    .setCellValue(i * 10);
            }

            workbook.write(request.response()
                .getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
*/
        var workbook = new SXSSFWorkbook(100);
        Sheet sheet = workbook.createSheet("Report-Activity");

        for (int i = 0; i < 10000; i++) {
            Row row = sheet.createRow(i);
            Cell cell = row.createCell(0);
            cell.setCellValue("Data " + (i + 1));
        }

        try {
            OutputStream out = request.response()
                .getOutputStream();

            workbook.write(out);
            out.flush();
            out.close();

            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Builder
    public record Request(HttpServletResponse response) {

    }
}
