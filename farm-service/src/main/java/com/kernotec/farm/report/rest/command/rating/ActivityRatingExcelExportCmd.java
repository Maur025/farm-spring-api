package com.kernotec.farm.report.rest.command.rating;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.report.jpa.service.ReportActivityService;
import com.kernotec.farm.report.rest.command.excel.ReportHeadCreateCmd;
import com.kernotec.farm.report.rest.dto.request.ReportRatingRequest;
import com.kernotec.farm.report.rest.dto.response.rating.ReportRatingResponse;
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
    private final ReportActivityService reportActivityService;

    @Override
    protected Void run(Request request) {
        Sheet sheet = request.sheet();
        ReportRatingRequest filterRequest = request.filterRequest();

        excelUtil.setColumnsSize(sheet, List.of(6, 40, 30));

        int headerRowIdx = reportHeadCreateCmd.withRequest(ReportHeadCreateCmd.Request.builder()
                .sheet(sheet)
                .reportTitle(request.reportTitle())
                .authUsername(request.authUsername())
                .zoneId(filterRequest.getZoneId())
                .searchDateDetail(filterRequest.getSearchCriteria())
                .searchDateDetail("pensando")
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
        excelUtil.fillRowSingleColumn(sheet, subTitle, true, rowIdx++);

        Row headerRow = sheet.createRow(rowIdx++);
        excelUtil.fillExcelRow(
            headerRow, true, List.of("#", "Nombre", "Cantidad de actividades"), true);

        List<ReportRatingResponse> ratingResponseList = reportActivityService.getActivitiesRatings(
            request.filterRequest(), isDescending);

        int rowCount = 0;
        for (ReportRatingResponse reportRatingResponse : ratingResponseList) {
            Row row = sheet.createRow(rowIdx + rowCount);

            rowCount++;

            excelUtil.fillExcelRow(
                row, false, List.of(
                    String.valueOf(rowCount), reportRatingResponse.getRatingByName(),
                    reportRatingResponse.getTotalActivities()
                        .toString()
                ), false
            );
        }

        return rowIdx + rowCount;
    }

    @Builder
    public record Request(Sheet sheet, String reportTitle, ReportRatingRequest filterRequest,
                          String authUsername)
    {

    }
}
