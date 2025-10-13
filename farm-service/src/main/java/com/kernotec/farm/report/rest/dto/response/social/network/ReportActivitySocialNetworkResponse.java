package com.kernotec.farm.report.rest.dto.response.social.network;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
public class ReportActivitySocialNetworkResponse extends EntityResponse {

    private UUID socialNetworkId;
    private String socialNetworkName;
    private String socialNetworkCode;
    private String socialNetworkIcon;
    private String socialNetworkColor;
    private Long totalActivities;
}
