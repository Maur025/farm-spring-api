package com.kernotec.farm.report.rest.dto.response.account;

import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GroupSummaryResponse extends EntityResponse {

    private Long totalGroups;
    private List<GroupRegionSummaryResponse> totalsByRegion;
    private List<AccountSummaryTableResponse> groups;
}