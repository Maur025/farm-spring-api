package com.kernotec.farm.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernotec.farm.config.KernotecApiDefinition;
import java.io.ByteArrayInputStream;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
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
public class PdfUtil {

    private final KernotecApiDefinition kernotecApiDefinition;

    public String consumeUrlAndGetData(String sourceUrl, String token,
        Supplier<StringBuilder> callbackGetParams)
    {
        var restTemplate = new RestTemplate();

        var url = new StringBuilder();
        url.append(kernotecApiDefinition.getServers()
                .get(0)
                .getUrl())
            .append("/api/farm/")
            .append(sourceUrl);

        StringBuilder urlParams = callbackGetParams.get();

        if (!urlParams.isEmpty()) {
            url.append("?")
                .append(urlParams);
        }

        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Accept", "application/json");

        var entity = new HttpEntity<>(headers);

        log.info("Consuming URL: {}", url);

        ResponseEntity<String> response = restTemplate.exchange(
            url.toString(), HttpMethod.GET, entity, String.class);

        return response.getBody() != null ? response.getBody() : "[]";
    }

    public JsonDataSource getJsonDataSourceFromJsonString(String jsonString) {
        var objectMapper = new ObjectMapper();

        try {
            JsonNode root = objectMapper.readTree(jsonString);
            JsonNode dataArray = root.path("data");

            return new JsonDataSource(
                new ByteArrayInputStream(objectMapper.writeValueAsBytes(dataArray)));
        } catch (JRException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUrlParam(StringBuilder queryParams, String key, String value) {
        if (!queryParams.isEmpty()) {
            queryParams.append("&");
        }

        queryParams.append(key)
            .append("=")
            .append(value);
    }
}
