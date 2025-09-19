package com.kernotec.farm.report.rest.dto.response.account;

import java.util.List;
import lombok.Builder;

@Builder
public record CommentSummaryResponse(Long totalComments, Long totalPositiveComments,
                                     Long totalNegativeComments,
                                     List<AccountSummaryTableResponse> comments)
{

    public CommentSummaryResponse(Long totalComments, Long totalPositiveComments,
        Long totalNegativeComments)
    {
        this(totalComments, totalPositiveComments, totalNegativeComments, null);
    }

    public CommentSummaryResponse withComments(List<AccountSummaryTableResponse> comments) {
        return new CommentSummaryResponse(
            this.totalComments, this.totalPositiveComments,
            this.totalNegativeComments, comments
        );
    }
}
