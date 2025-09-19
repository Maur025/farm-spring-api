package com.kernotec.farm.report.rest.dto.response.account;

import lombok.Builder;

@Builder
public record CommentSummaryResponse(Long totalComments, Long totalPositiveComments,
                                     Long totalNegativeComments)
{

}
