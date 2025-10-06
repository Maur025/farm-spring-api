package com.kernotec.farm.report.rest.command.excel;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.farm.util.ExcelUtil;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportHeadCreateCmd extends AbstractCommand<ReportHeadCreateCmd.Request, Integer> {

    private final ExcelUtil excelUtil;

    @Override
    protected Integer run(Request request) {
        Sheet sheet = request.sheet();
        int rowNum = 0;

        excelUtil.fillRowSingleColumn(
            sheet, request.reportTitle() == null ? "Activity Report" : request.reportTitle(), true,
            rowNum
        );

        excelUtil.fillRowSingleColumn(
            sheet, "Generado por: " + request.authUsername(), true, rowNum++);

        String reportDate = excelUtil.getDateFormat(
            ZonedDateTime.now(ZoneOffset.UTC),
            request.zoneId(), "dd-MM-yyyy HH:mm"
        );
        excelUtil.fillRowSingleColumn(sheet, "Fecha emisión: " + reportDate, false, rowNum++);

        String searchCriteria = request.searchCriteria() == null || request.searchCriteria()
            .isEmpty() ? "Sin criterios de busqueda" : request.searchCriteria();
        excelUtil.fillRowSingleColumn(
            sheet, "Criterios de busqueda: " + searchCriteria, false, rowNum++);

        String reportDateDetail = request.searchDateDetail() == null || request.searchDateDetail()
            .isEmpty() ? "Sin filtro de fechas" : request.searchDateDetail();
        excelUtil.fillRowSingleColumn(sheet, reportDateDetail, false, rowNum++);

        rowNum++;
        return rowNum;
    }

    @Builder
    public record Request(Sheet sheet, String reportTitle, String authUsername, String zoneId,
                          String searchCriteria, String searchDateDetail)
    {

    }
}
