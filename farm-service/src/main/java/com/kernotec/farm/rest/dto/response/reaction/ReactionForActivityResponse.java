package com.kernotec.farm.rest.dto.response.reaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.rest.dto.response.reaction.type.ReactionTypeResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ReactionForActivityResponse extends EntityResponse {

    private UUID reactionTypeId;
    private ReactionTypeResponse reactionType;
}
