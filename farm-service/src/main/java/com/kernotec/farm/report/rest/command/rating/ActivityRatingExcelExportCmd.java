package com.kernotec.farm.report.rest.command.rating;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.report.rest.command.excel.ReportHeadCreateCmd;
import com.kernotec.farm.report.rest.dto.request.ReportRatingRequest;
import com.kernotec.farm.util.ExcelUtil;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ActivityRatingExcelExportCmd extends
    AbstractTransactionalRequiredCommand<ActivityRatingExcelExportCmd.Request, Void>
{

    private final ExcelUtil excelUtil;
    private final ReportHeadCreateCmd reportHeadCreateCmd;

    @Override
    protected Void run(Request request) {
        Sheet sheet = request.sheet();
        ReportRatingRequest filterRequest = request.filterRequest();

        excelUtil.setColumnsSize(sheet, List.of(6, 20, 26));

        int headerRowIdx = reportHeadCreateCmd.withRequest(ReportHeadCreateCmd.Request.builder()
                .sheet(sheet)
                .reportTitle(request.reportTitle())
                .authUsername(request.authUsername())
                .zoneId(filterRequest.getZoneId())
                .searchDateDetail(filterRequest.getSearchCriteria())
                .searchDateDetail("pensando")
                .build())
            .execute();

        excelUtil.fillRowSingleColumn(
            sheet, "Clasificación con la mayor cantidad de actividades", true, headerRowIdx++);

        Row headerRowMore = sheet.createRow(headerRowIdx++);
        excelUtil.fillExcelRow(
            headerRowMore, true, List.of("#", "Nombre", "Cantidad de actividades"), true);

        excelUtil.fillRowSingleColumn(
            sheet, "Clasificación con la menor cantidad de actividades", true, headerRowIdx++);

        Row headerRowLess = sheet.createRow(headerRowIdx++);
        excelUtil.fillExcelRow(
            headerRowLess, true, List.of("#", "Nombre", "Cantidad de actividades"), true);

        return null;
    }

    @Builder
    public record Request(Sheet sheet, String reportTitle, String sortBy, boolean descending,
                          ReportRatingRequest filterRequest, String authUsername)
    {

    }
}
