package com.kernotec.farm.report.rest.command.activity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.config.KernotecApiDefinition;
import com.kernotec.farm.report.jpa.service.ReportActivityService;
import com.kernotec.farm.report.rest.dto.request.ReportActivityRequest;
import com.kernotec.farm.util.ResourceUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JsonDataSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportActivityPdfExportCmd extends
    AbstractTransactionalRequiredCommand<ReportActivityPdfExportCmd.Request, Void>
{

    private final KernotecApiDefinition kernotecApiDefinition;
    private final ReportActivityService reportActivityService;

    @Override
    protected Void run(Request request) {
        var restTemplate = new RestTemplate();
        var url = new StringBuilder();
        url.append(kernotecApiDefinition.getServers()
                .get(0)
                .getUrl())
            .append("/api/farm/reports/activities/report/unpaginated");

        buildParamsToUrl(url, request.filterRequest(), request.sortBy(), request.isDescending());

        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + request.token());
        headers.set("Accept", "application/json");

        var entity = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            url.toString(), HttpMethod.GET, entity, String.class);

        log.info("url: {}", url);

        String jsonData = response.getBody() != null ? response.getBody() : "[]";

        var objectMapper = new ObjectMapper();
        JsonNode dataArray;
        try {
            JsonNode root = objectMapper.readTree(jsonData);
            dataArray = root.path("data");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonDataSource jsonDataSource;
        try {
            jsonDataSource = new JsonDataSource(
                new ByteArrayInputStream(objectMapper.writeValueAsBytes(dataArray)));

            log.info("jsonDataSource: {}", jsonDataSource.getTextAttributes());
        } catch (JRException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String jasperFilePath;
        try {
            jasperFilePath = ResourceUtil.getAbsolutePath("MyReports/ActivityReport.jasper");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ReportActivityRequest filterRequest = request.filterRequest();

        Map<String, Object> params = getParams(request, filterRequest);

        try (OutputStream out = request.response()
            .getOutputStream())
        {
            byte[] reportPdfBytes = JasperRunManager.runReportToPdf(
                jasperFilePath, params, jsonDataSource);

            request.response()
                .setContentLength(reportPdfBytes.length);

            out.write(reportPdfBytes);
            out.flush();
        } catch (JRException | IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private Map<String, Object> getParams(Request request, ReportActivityRequest filterRequest)
    {
        String reportDateDetail = reportActivityService.getReportDateDetail(filterRequest);

        Map<String, Object> params = new HashMap<>();
        params.put("AUTH_USERNAME", request.authUsername());
        params.put("TITLE", request.titleReport());
        params.put(
            "SEARCH_CRITERIA",
            filterRequest.getSearchCriteria() == null || filterRequest.getSearchCriteria()
                .isEmpty() ? "Sin criterios de busqueda" : filterRequest.getSearchCriteria()
        );
        params.put("DATE_DETAIL", reportDateDetail);
        params.put("REPORT_ZONE_ID", filterRequest.getZoneId());
        params.put(
            "REPORT_LOGO", this.getClass()
                .getResourceAsStream("/images/logotipoKD.png")
        );
        return params;
    }

    private void buildParamsToUrl(StringBuilder urlBuilder, ReportActivityRequest filterRequest,
        String sortBy, boolean isDescending)
    {
        var queryParams = new StringBuilder();
        addParam(queryParams, "descending", String.valueOf(isDescending));

        if (sortBy != null) {
            addParam(queryParams, "sortBy", sortBy);
        }

        if (filterRequest.getZoneId() != null) {
            addParam(queryParams, "zoneId", filterRequest.getZoneId());
        }

        if (filterRequest.getUserAuthId() != null) {
            addParam(
                queryParams, "userAuthId", filterRequest.getUserAuthId()
                    .toString()
            );
        }

        if (filterRequest.getSocialNetworkId() != null) {
            addParam(
                queryParams, "socialNetworkId", filterRequest.getSocialNetworkId()
                    .toString()
            );
        }

        if (filterRequest.getActivityTypeId() != null) {
            addParam(
                queryParams, "activityTypeId", filterRequest.getActivityTypeId()
                    .toString()
            );
        }

        if (filterRequest.getFarmId() != null) {
            addParam(
                queryParams, "farmId", filterRequest.getFarmId()
                    .toString()
            );
        }

        if (filterRequest.getDeviceId() != null) {
            addParam(
                queryParams, "deviceId", filterRequest.getDeviceId()
                    .toString()
            );
        }

        if (filterRequest.getAccountId() != null) {
            addParam(
                queryParams, "accountId", filterRequest.getAccountId()
                    .toString()
            );
        }

        if (filterRequest.getSimpleDate() != null) {
            addParam(
                queryParams, "simpleDate", filterRequest.getSimpleDate()
                    .toString()
            );
        }

        if (filterRequest.getFromDate() != null) {
            addParam(
                queryParams, "fromDate", filterRequest.getFromDate()
                    .toString()
            );
        }

        if (filterRequest.getToDate() != null) {
            addParam(
                queryParams, "toDate", filterRequest.getToDate()
                    .toString()
            );
        }

        if (filterRequest.getMonthDate() != null) {
            addParam(
                queryParams, "monthDate", filterRequest.getMonthDate()
                    .toString()
            );
        }

        if (filterRequest.getYearDate() != null) {
            addParam(
                queryParams, "yearDate", filterRequest.getYearDate()
                    .toString()
            );
        }

        if (queryParams.isEmpty()) {
            return;
        }

        urlBuilder.append("?")
            .append(queryParams);
    }

    private void addParam(StringBuilder queryParams, String key, String value) {
        if (!queryParams.isEmpty()) {
            queryParams.append("&");
        }

        queryParams.append(key)
            .append("=")
            .append(value);
    }

    @Builder
    public record Request(String token, ReportActivityRequest filterRequest, String sortBy,
                          boolean isDescending, String authUsername, String titleReport,
                          HttpServletResponse response)
    {

    }
}
