package com.kernotec.farm.report.rest.command.rating;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.report.jpa.service.ReportActivityService;
import com.kernotec.farm.report.rest.command.excel.ReportHeadCreateCmd;
import com.kernotec.farm.report.rest.dto.request.ReportRatingRequest;
import com.kernotec.farm.report.rest.dto.response.rating.ReportRatingResponse;
import com.kernotec.farm.util.ExcelUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ActivityRatingExcelExportCmd extends
    AbstractTransactionalRequiredCommand<ActivityRatingExcelExportCmd.Request, Void>
{

    private final ExcelUtil excelUtil;
    private final ReportHeadCreateCmd reportHeadCreateCmd;
    private final ReportActivityService reportActivityService;
    private final Map<ActivityRatingStyleEnum, CellStyle> cellStyles = new HashMap<>();

    @Override
    protected Void run(Request request) {
        Sheet sheet = request.workbook.createSheet("Reporte actividades por clasificación");
        ReportRatingRequest filterRequest = request.filterRequest();

        cellStyles.put(
            ActivityRatingStyleEnum.HEADER, excelUtil.getCellStyle(request.workbook, true, true));
        cellStyles.put(
            ActivityRatingStyleEnum.CONTENT,
            excelUtil.getCellStyle(request.workbook, false, false)
        );
        cellStyles.put(
            ActivityRatingStyleEnum.BOLD, excelUtil.getCellStyle(request.workbook, true, false));

        excelUtil.setColumnsSize(sheet, List.of(6, 40, 30));

        int headerRowIdx = reportHeadCreateCmd.withRequest(ReportHeadCreateCmd.Request.builder()
                .sheet(sheet)
                .reportTitle(request.reportTitle())
                .authUsername(request.authUsername())
                .zoneId(filterRequest.getZoneId())
                .searchCriteria(filterRequest.getSearchCriteria())
                .searchDateDetail(reportActivityService.getReportDateDetail(filterRequest))
                .build())
            .execute();

        headerRowIdx = setTableWithData(
            sheet, headerRowIdx, request,
            "Clasificación con la mayor cantidad de actividades", true
        );

        headerRowIdx++;
        headerRowIdx++;

        setTableWithData(
            sheet, headerRowIdx, request, "Clasificación con la menor cantidad de actividades",
            false
        );

        return null;
    }

    private int setTableWithData(Sheet sheet, int rowIdx, Request request, String subTitle,
        boolean isDescending)
    {
        excelUtil.fillRowSingleColumn(
            sheet, subTitle, rowIdx++, cellStyles.get(ActivityRatingStyleEnum.BOLD));

        Row headerRow = sheet.createRow(rowIdx++);
        excelUtil.fillExcelRow(
            headerRow, List.of("#", "Nombre", "Cantidad de actividades"),
            cellStyles.get(ActivityRatingStyleEnum.HEADER)
        );

        List<ReportRatingResponse> ratingResponseList = reportActivityService.getActivitiesRatings(
            request.filterRequest(), isDescending);

        int rowCount = 0;
        for (ReportRatingResponse reportRatingResponse : ratingResponseList) {
            Row row = sheet.createRow(rowIdx + rowCount);

            rowCount++;

            excelUtil.fillExcelRow(
                row, List.of(
                    String.valueOf(rowCount), reportRatingResponse.getRatingByName(),
                    reportRatingResponse.getTotalActivities()
                        .toString()
                ), cellStyles.get(ActivityRatingStyleEnum.CONTENT)
            );
        }

        return rowIdx + rowCount;
    }

    @Builder
    public record Request(SXSSFWorkbook workbook, String reportTitle,
                          ReportRatingRequest filterRequest, String authUsername)
    {

    }
}
