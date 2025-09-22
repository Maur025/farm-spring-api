package com.kernotec.farm.report.rest.dto.response.account;

import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.List;
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
    private List<AccountSummaryTableResponse> comments;


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
