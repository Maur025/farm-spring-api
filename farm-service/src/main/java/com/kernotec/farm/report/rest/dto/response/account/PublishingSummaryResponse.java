package com.kernotec.farm.report.rest.dto.response.account;

import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PublishingSummaryResponse extends EntityResponse {

    private Long totalPublications;
    private List<PublishingContextSummaryResponse> totalsByContext;
    private PageResponse<AccountSummaryTableResponse> publications;
}
