package com.kernotec.farm.report.rest.dto.response.account;

import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FriendSummaryResponse extends EntityResponse {

    private Long totalFriends;
    private Long totalInternalFriends;
    private Long totalExternalFriends;
    private List<AccountSummaryTableResponse> friends;

    public FriendSummaryResponse(Long totalFriends, Long totalInternalFriends,
        Long totalExternalFriends, List<AccountSummaryTableResponse> friends)
    {
        this.totalFriends = totalFriends;
        this.totalInternalFriends = totalInternalFriends;
        this.totalExternalFriends = totalExternalFriends;
        this.friends = friends;
    }

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
