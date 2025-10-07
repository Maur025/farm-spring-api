package com.kernotec.farm.report.rest.command.rating;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.report.jpa.service.ReportActivityService;
import com.kernotec.farm.report.rest.command.pdf.PdfBuildJasperParamCmd;
import com.kernotec.farm.report.rest.dto.request.ReportRatingRequest;
import com.kernotec.farm.util.PdfUtil;
import com.kernotec.farm.util.ResourceUtil;
import java.io.IOException;
import java.util.Map;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JsonDataSource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ActivityRatingPdfExportCmd extends
    AbstractTransactionalRequiredCommand<ActivityRatingPdfExportCmd.Request, byte[]>
{

    private final PdfUtil pdfUtil;
    private final PdfBuildJasperParamCmd pdfBuildJasperParamCmd;
    private final ReportActivityService reportActivityService;

    @Override
    protected byte[] run(Request request) {
        ReportRatingRequest filterRequest = request.filterRequest();

        String jsonResponseData = pdfUtil.consumeUrlAndGetData(
            "reports/activities/ratings/report",
            request.token(), () -> buildParamsToUrl(filterRequest)
        );

        JsonDataSource jsonDataSource = pdfUtil.getJsonDataSourceFromJsonString(jsonResponseData);

        Map<String, Object> params = pdfBuildJasperParamCmd.withRequest(
                PdfBuildJasperParamCmd.Request.builder()
                    .authUsername(request.authUsername())
                    .titleReport(request.titleReport())
                    .searchCriteria(filterRequest.getSearchCriteria())
                    .searchDateDetail(reportActivityService.getReportDateDetail(filterRequest))
                    .zoneId(filterRequest.getZoneId())
                    .build())
            .execute();

        try {
            String jasperFilePath = ResourceUtil.getAbsolutePath(
                "MyReports/ActivityRatingReport.jasper");

            return JasperRunManager.runReportToPdf(jasperFilePath, params, jsonDataSource);
        } catch (IOException | JRException e) {
            throw new RuntimeException(e);
        }
    }

    private StringBuilder buildParamsToUrl(ReportRatingRequest filterRequest)
    {
        var queryParams = new StringBuilder();

        if (filterRequest.getZoneId() != null) {
            pdfUtil.addUrlParam(queryParams, "zoneId", filterRequest.getZoneId());
        }

        if (filterRequest.getSocialNetworkId() != null) {
            pdfUtil.addUrlParam(
                queryParams, "socialNetworkId", filterRequest.getSocialNetworkId()
                    .toString()
            );
        }

        if (filterRequest.getRatingType() != null) {
            pdfUtil.addUrlParam(
                queryParams, "ratingType", filterRequest.getRatingType()
                    .toString()
            );
        }

        if (filterRequest.getLimit() != null) {
            pdfUtil.addUrlParam(
                queryParams, "limit", filterRequest.getLimit()
                    .toString()
            );
        }

        if (filterRequest.getSimpleDate() != null) {
            pdfUtil.addUrlParam(
                queryParams, "simpleDate", filterRequest.getSimpleDate()
                    .toString()
            );
        }

        if (filterRequest.getFromDate() != null) {
            pdfUtil.addUrlParam(
                queryParams, "fromDate", filterRequest.getFromDate()
                    .toString()
            );
        }

        if (filterRequest.getToDate() != null) {
            pdfUtil.addUrlParam(
                queryParams, "toDate", filterRequest.getToDate()
                    .toString()
            );
        }

        if (filterRequest.getMonthDate() != null) {
            pdfUtil.addUrlParam(
                queryParams, "monthDate", filterRequest.getMonthDate()
                    .toString()
            );
        }

        return queryParams;
    }

    @Builder
    public record Request(String token, ReportRatingRequest filterRequest, String authUsername,
                          String titleReport)
    {

    }
}
