package com.kernotec.farm.report.rest.dto.response.account;

import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PageSummaryResponse extends EntityResponse {

    private Long totalPages;
    private List<PageRegionSummaryResponse> totalsByRegion;
    private List<AccountSummaryTableResponse> profiles;
}
