package com.kernotec.farm.report.rest.dto.response.account;

import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReactionSummaryResponse extends EntityResponse {

    private Long totalReactions;
    private List<ReactionTypeSummaryResponse> totalsByType;
}
