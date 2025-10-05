package com.kernotec.farm.report.rest.dto.response.social.network;

import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReportActivityGroupBySocialNetworkResponse extends EntityResponse {

    private Long totalActivities;
    private List<ReportActivitySocialNetworkResponse> socialNetworks;
}
