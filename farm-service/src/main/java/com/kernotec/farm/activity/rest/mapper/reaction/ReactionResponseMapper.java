package com.kernotec.farm.activity.rest.mapper.reaction;

import com.kernotec.farm.activity.jpa.entity.Reaction;
import com.kernotec.farm.activity.rest.dto.response.reaction.ReactionResponse;
import com.kernotec.farm.activity.rest.mapper.activity.ActivityResponseFlatMapper;
import com.kernotec.farm.parametric.rest.mapper.activity.type.ActivityTypeResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {ActivityResponseFlatMapper.class, ActivityTypeResponseFlatMapper.class})
public interface ReactionResponseMapper {

    ReactionResponse toResponse(Reaction reaction);

    ReactionResponse toResponse(UUID id);

    List<ReactionResponse> toResponse(List<Reaction> reactionList);

    Set<ReactionResponse> toResponse(Set<Reaction> reactionSet);
}
