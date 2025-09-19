package com.kernotec.farm.report.rest.dto.response.account;

import java.util.List;
import lombok.Builder;

@Builder
public record PublishingSummaryResponse(Long totalPublications,
                                        List<PublishingContextSummaryResponse> totalsByContext,
                                        List<AccountSummaryTableResponse> publications)
{

}
