package com.kernotec.farm.report.rest.command.activity;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import java.io.FileOutputStream;
import java.io.IOException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
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
        var workbook = new SXSSFWorkbook(100);

        try (var out = new FileOutputStream("report-activity.xlsx")) {
            SXSSFSheet sheet = workbook.createSheet("Report-Activity");

            for (int i = 0; i < 10; i++) {
                Row row = sheet.createRow(i);
                row.createCell(0)
                    .setCellValue("Data " + i);
                row.createCell(1)
                    .setCellValue(i * 10);
            }

            workbook.write(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Builder
    public record Request() {

    }
}
