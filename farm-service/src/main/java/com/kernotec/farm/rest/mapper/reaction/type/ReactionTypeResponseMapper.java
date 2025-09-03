package com.kernotec.farm.rest.mapper.reaction.type;

import com.kernotec.farm.parametric.jpa.entity.ReactionType;
import com.kernotec.farm.rest.dto.response.reaction.type.ReactionTypeResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface ReactionTypeResponseMapper {

    ReactionTypeResponse toResponse(UUID id);

    ReactionTypeResponse toResponse(ReactionType reactionType);

    List<ReactionTypeResponse> toResponse(List<ReactionType> reactionTypeList);

    Set<ReactionTypeResponse> toResponse(Set<ReactionType> reactionTypeSet);
}
