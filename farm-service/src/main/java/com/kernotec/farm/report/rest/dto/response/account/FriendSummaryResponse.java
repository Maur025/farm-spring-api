package com.kernotec.farm.report.rest.dto.response.account;

import lombok.Builder;

@Builder
public record FriendSummaryResponse(Long totalFriends, Long totalInternalFriends,
                                    Long totalExternalFriends)
{

}
