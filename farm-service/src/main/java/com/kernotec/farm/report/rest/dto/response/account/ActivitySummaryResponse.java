package com.kernotec.farm.report.rest.dto.response.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class ActivitySummaryResponse extends EntityResponse {

    private final UUID accountId;
    private final String accountName;

    private final UUID socialNetworkId;
    private final String socialNetworkName;

    private final FriendSummaryResponse friendSummary;
    private final GroupSummaryResponse groupSummary;
    private final PageSummaryResponse pageSummary;
    private final PublishingSummaryResponse publishingSummary;
    private final ReactionSummaryResponse reactionSummary;
    private final CommentSummaryResponse commentSummary;
}
