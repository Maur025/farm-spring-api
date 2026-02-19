package com.kernotec.farm.report.rest.command.excel;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.farm.report.rest.command.activity.ReportActivityStyleEnum;
import com.kernotec.farm.util.ExcelUtil;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
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

        CellStyle boldStyle = request.cellStyles.get(ReportActivityStyleEnum.BOLD);
        CellStyle contentStyle = request.cellStyles.get(ReportActivityStyleEnum.CONTENT);

        excelUtil.fillRowSingleColumn(
            sheet,
            request.reportTitle() == null ? "Activity Report" : request.reportTitle(), rowNum++,
            boldStyle
        );

        excelUtil.fillRowSingleColumn(
            sheet, "Generado por: " + request.authUsername(), rowNum++, boldStyle);

        String reportDate = excelUtil.getDateFormat(
            ZonedDateTime.now(ZoneOffset.UTC),
            request.zoneId(), "dd-MM-yyyy HH:mm"
        );
        excelUtil.fillRowSingleColumn(
            sheet, "Fecha emisión: " + reportDate, rowNum++, contentStyle);

        String searchCriteria = request.searchCriteria() == null || request.searchCriteria()
            .isEmpty() ? "Sin criterios de busqueda" : request.searchCriteria();
        excelUtil.fillRowSingleColumn(
            sheet, "Criterios de busqueda: " + searchCriteria, rowNum++, contentStyle);

        String reportDateDetail = request.searchDateDetail() == null || request.searchDateDetail()
            .isEmpty() ? "Sin filtro de fechas" : request.searchDateDetail();
        excelUtil.fillRowSingleColumn(sheet, reportDateDetail, rowNum++, contentStyle);

        rowNum++;
        return rowNum;
    }

    @Builder
    public record Request(Sheet sheet, String reportTitle, String authUsername, String zoneId,
                          String searchCriteria, String searchDateDetail,
                          Map<ReportActivityStyleEnum, CellStyle> cellStyles)
    {

    }
}
