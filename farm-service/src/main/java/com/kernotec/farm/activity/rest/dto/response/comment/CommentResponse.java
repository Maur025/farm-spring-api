package com.kernotec.farm.activity.rest.dto.response.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.activity.rest.dto.response.activity.ActivityFlatResponse;
import com.kernotec.farm.parametric.rest.dto.response.activity.type.ActivityTypeForActivityResponse;
import com.kernotec.farm.parametric.rest.dto.response.publishing.context.PublishingContextResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class CommentResponse extends EntityResponse {

    private String comment;
    private Boolean isAgreeComment;

    private UUID publishingContextId;
    private PublishingContextResponse publishingContext;

    private UUID activityId;
    private ActivityFlatResponse activity;

    private UUID activityTypeId;
    private ActivityTypeForActivityResponse activityType;
}
