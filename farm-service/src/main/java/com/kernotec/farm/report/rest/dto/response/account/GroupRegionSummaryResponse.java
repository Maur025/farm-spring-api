package com.kernotec.farm.report.rest.dto.response.account;

import java.util.UUID;
import lombok.Builder;

@Builder
public record GroupRegionSummaryResponse(String regionName, UUID regionId,
                                         Long totalGroupsByRegion)
{

}
