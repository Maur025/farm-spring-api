package com.kernotec.farm.report.rest.dto.response.account;

import java.util.List;
import lombok.Builder;

@Builder
public record FriendSummaryResponse(Long totalFriends, Long totalInternalFriends,
                                    Long totalExternalFriends,
                                    List<AccountSummaryTableResponse> friends)
{

    public FriendSummaryResponse(Long totalFriends, Long totalInternalFriends,
        Long totalExternalFriends)
    {
        this(totalFriends, totalInternalFriends, totalExternalFriends, null);
    }

    public static FriendSummaryResponse of(Long totalFriends, Long totalInternalFriends,
        Long totalExternalFriends)
    {
        return new FriendSummaryResponse(
            totalFriends, totalInternalFriends, totalExternalFriends,
            null
        );
    }

    public FriendSummaryResponse withFriends(List<AccountSummaryTableResponse> friends) {
        return new FriendSummaryResponse(
            this.totalFriends, this.totalInternalFriends,
            this.totalExternalFriends, friends
        );
    }
}
