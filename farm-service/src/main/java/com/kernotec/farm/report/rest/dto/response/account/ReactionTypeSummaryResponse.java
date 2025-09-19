package com.kernotec.farm.report.rest.dto.response.account;

import java.util.UUID;
import lombok.Builder;

@Builder
public record ReactionTypeSummaryResponse(UUID reactionTypeId, String reactionTypeName,
                                          String reactionTypeCode, Long totalReactionsByType)
{

}
