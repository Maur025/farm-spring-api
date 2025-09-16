package com.kernotec.farm.activity.rest.dto.response.reaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.activity.rest.dto.response.activity.ActivityResponse;
import com.kernotec.farm.parametric.rest.dto.response.activity.type.ActivityTypeResponse;
import com.kernotec.farm.parametric.rest.dto.response.reaction.type.ReactionTypeResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ReactionResponse extends EntityResponse {

    private UUID reactionTypeId;
    private ReactionTypeResponse reactionType;

    private UUID activityId;
    private ActivityResponse activity;

    private UUID activityTypeId;
    private ActivityTypeResponse activityType;
}
