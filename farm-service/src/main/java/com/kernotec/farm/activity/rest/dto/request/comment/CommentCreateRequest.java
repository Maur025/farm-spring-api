package com.kernotec.farm.activity.rest.dto.request.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class CommentCreateRequest extends BaseRequest {

    private String comment;
    private Boolean isAgreeComment;
    private UUID publishingContextId;
}
