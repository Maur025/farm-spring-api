package com.kernotec.farm.report.rest.dto.response.account;

import java.util.List;
import lombok.Builder;

@Builder
public record PageSummaryResponse(Long totalPages, List<PageRegionSummaryResponse> totalsByRegion) {

}
