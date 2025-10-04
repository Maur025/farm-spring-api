package com.kernotec.farm.report.rest.dto.response.farm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.farm.report.rest.dto.response.activity.ActivityTypeTotalResponse;
import java.util.UUID;
import lombok.Getter;

@Getter
@JsonInclude(Include.NON_NULL)
public class FarmReportTotalResponse extends ActivityTypeTotalResponse {

    private final UUID farmId;
    private final String name;

    public FarmReportTotalResponse(UUID farmId, String name, Long totalActivities,
        Long totalComments, Long totalGroups, Long totalFriends, Long totalPageFollows,
        Long totalReactions, Long totalPublications, Long totalTiktokProfileFollows)
    {
        super(
            totalActivities, totalComments, totalGroups, totalFriends, totalPageFollows,
            totalReactions, totalPublications, totalTiktokProfileFollows
        );

        this.farmId = farmId;
        this.name = name;
    }
}
