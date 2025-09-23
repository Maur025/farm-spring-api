package com.kernotec.farm.report.rest.dto.response.account;

import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class FriendSummaryResponse extends EntityResponse {

    private Long totalFriends;
    private Long totalInternalFriends;
    private Long totalExternalFriends;
    private PageResponse<AccountSummaryTableResponse> friends;

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

    public FriendSummaryResponse withFriends(PageResponse<AccountSummaryTableResponse> friends) {
        return new FriendSummaryResponse(
            this.totalFriends, this.totalInternalFriends,
            this.totalExternalFriends, friends
        );
    }
}
