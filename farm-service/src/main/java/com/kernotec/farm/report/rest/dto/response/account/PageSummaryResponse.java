package com.kernotec.farm.report.rest.dto.response.account;

import com.kernotec.farm.activity.jpa.dto.entity.ProfileDto;
import java.util.List;
import lombok.Builder;

@Builder
public record PageSummaryResponse(Long totalPages, List<PageRegionSummaryResponse> totalsByRegion,
                                  List<ProfileDto> profiles)
{

}
