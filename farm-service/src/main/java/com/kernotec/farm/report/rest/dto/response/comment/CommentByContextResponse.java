package com.kernotec.farm.report.rest.dto.response.comment;

import java.util.UUID;

public record CommentByContextResponse(UUID publishingContextId, String publishingContextName,
                                       Long totalCommentsByContext)
{

}
