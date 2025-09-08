package com.kernotec.farm.activity.rest.mapper.reaction;

import com.kernotec.farm.activity.jpa.entity.Reaction;
import com.kernotec.farm.activity.rest.dto.response.reaction.ReactionResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReactionResponseForActivityMapper {

    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    ReactionResponse toResponse(Reaction reaction);

    ReactionResponse toResponse(UUID id);

    List<ReactionResponse> toResponse(List<Reaction> reactionList);

    Set<ReactionResponse> toResponse(Set<Reaction> reactionSet);
}
