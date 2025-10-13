package com.kernotec.farm.report.rest.command.activity;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.report.jpa.service.ReportActivityService;
import com.kernotec.farm.report.rest.command.pdf.PdfBuildJasperParamCmd;
import com.kernotec.farm.report.rest.dto.request.ReportActivityRequest;
import com.kernotec.farm.util.PdfUtil;
import com.kernotec.farm.util.ResourceUtil;
import java.io.IOException;
import java.util.Map;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JsonDataSource;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportActivityPdfExportCmd extends
    AbstractTransactionalRequiredCommand<ReportActivityPdfExportCmd.Request, byte[]>
{

    private final ReportActivityService reportActivityService;
    private final PdfUtil pdfUtil;
    private final PdfBuildJasperParamCmd pdfBuildJasperParamCmd;

    @Override
    protected byte[] run(Request request) {
        ReportActivityRequest filterRequest = request.filterRequest();

        String jsonResponseData = pdfUtil.consumeUrlAndGetData(
            "reports/activities/report/unpaginated", request.token(),
            () -> buildParamsToUrl(filterRequest, request.sortBy(), request.isDescending())
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
            String jasperFilePath = ResourceUtil.getAbsolutePath("MyReports/ActivityReport.jasper");

            return JasperRunManager.runReportToPdf(jasperFilePath, params, jsonDataSource);
        } catch (IOException | JRException ex) {
            throw new RuntimeException(ex);
        }
    }

    private StringBuilder buildParamsToUrl(ReportActivityRequest filterRequest, String sortBy,
        boolean isDescending)
    {
        var queryParams = new StringBuilder();
        pdfUtil.addUrlParam(queryParams, "descending", String.valueOf(isDescending));

        if (sortBy != null) {
            pdfUtil.addUrlParam(queryParams, "sortBy", sortBy);
        }

        if (filterRequest.getZoneId() != null) {
            pdfUtil.addUrlParam(queryParams, "zoneId", filterRequest.getZoneId());
        }

        if (filterRequest.getUserAuthId() != null) {
            pdfUtil.addUrlParam(
                queryParams, "userAuthId", filterRequest.getUserAuthId()
                    .toString()
            );
        }

        if (filterRequest.getSocialNetworkId() != null) {
            pdfUtil.addUrlParam(
                queryParams, "socialNetworkId", filterRequest.getSocialNetworkId()
                    .toString()
            );
        }

        if (filterRequest.getActivityTypeId() != null) {
            pdfUtil.addUrlParam(
                queryParams, "activityTypeId", filterRequest.getActivityTypeId()
                    .toString()
            );
        }

        if (filterRequest.getFarmId() != null) {
            pdfUtil.addUrlParam(
                queryParams, "farmId", filterRequest.getFarmId()
                    .toString()
            );
        }

        if (filterRequest.getDeviceId() != null) {
            pdfUtil.addUrlParam(
                queryParams, "deviceId", filterRequest.getDeviceId()
                    .toString()
            );
        }

        if (filterRequest.getAccountId() != null) {
            pdfUtil.addUrlParam(
                queryParams, "accountId", filterRequest.getAccountId()
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

        if (filterRequest.getYearDate() != null) {
            pdfUtil.addUrlParam(
                queryParams, "yearDate", filterRequest.getYearDate()
                    .toString()
            );
        }

        return queryParams;
    }

    @Builder
    public record Request(String token, ReportActivityRequest filterRequest, String sortBy,
                          boolean isDescending, String authUsername, String titleReport)
    {

    }
}
