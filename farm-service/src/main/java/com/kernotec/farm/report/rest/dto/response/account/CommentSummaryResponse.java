package com.kernotec.farm.report.rest.dto.response.account;

import com.kernotec.core.rest.dto.response.PageResponse;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CommentSummaryResponse extends EntityResponse {

    private Long totalComments;
    private Long totalPositiveComments;
    private Long totalNegativeComments;
    private PageResponse<AccountSummaryTableResponse> comments;


    public CommentSummaryResponse(Long totalComments, Long totalPositiveComments,
        Long totalNegativeComments)
    {
        this(totalComments, totalPositiveComments, totalNegativeComments, null);
    }

    public CommentSummaryResponse withComments(PageResponse<AccountSummaryTableResponse> comments) {
        return new CommentSummaryResponse(
            this.totalComments, this.totalPositiveComments,
            this.totalNegativeComments, comments
        );
    }
}
