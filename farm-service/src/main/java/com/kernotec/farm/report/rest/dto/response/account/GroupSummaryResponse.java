package com.kernotec.farm.report.rest.dto.response.account;

import java.util.List;
import lombok.Builder;

@Builder
public record GroupSummaryResponse(Long totalGroups,
                                   List<GroupRegionSummaryResponse> totalsByRegion)
{

}