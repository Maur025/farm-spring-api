package com.kernotec.farm.report.rest.dto.response.comment;

import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentGroupContextSummaryResponse extends EntityResponse {

    private Long totalComments;
    private List<CommentByContextResponse> totalsByContext;
}
