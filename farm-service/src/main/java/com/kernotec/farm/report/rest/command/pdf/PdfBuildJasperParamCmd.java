package com.kernotec.farm.report.rest.command.pdf;

import com.kernotec.core.command.AbstractCommand;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PdfBuildJasperParamCmd extends
    AbstractCommand<PdfBuildJasperParamCmd.Request, Map<String, Object>>
{

    @Override
    protected Map<String, Object> run(Request request) {
        Map<String, Object> params = new HashMap<>();
        params.put("AUTH_USERNAME", request.authUsername());
        params.put("TITLE", request.titleReport());
        params.put(
            "SEARCH_CRITERIA", request.searchCriteria() == null || request.searchCriteria()
                .isEmpty() ? "Sin criterios de busqueda" : request.searchCriteria()
        );
        params.put("DATE_DETAIL", request.searchDateDetail());
        params.put("REPORT_ZONE_ID", request.zoneId());
        params.put(
            "REPORT_LOGO", this.getClass()
                .getResourceAsStream("/images/logotipoKD.png")
        );

        return params;
    }

    @Builder
    public record Request(String authUsername, String titleReport, String searchCriteria,
                          String searchDateDetail, String zoneId)
    {

    }
}
