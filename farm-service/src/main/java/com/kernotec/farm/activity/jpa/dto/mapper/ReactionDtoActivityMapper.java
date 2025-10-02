package com.kernotec.farm.activity.jpa.dto.mapper;

import com.kernotec.farm.activity.jpa.dto.entity.ReactionDto;
import com.kernotec.farm.activity.jpa.entity.Reaction;
import com.kernotec.farm.parametric.jpa.dto.mapper.SocialNetworkDtoFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {SocialNetworkDtoFlatMapper.class})
public interface ReactionDtoActivityMapper {

    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    ReactionDto toDto(Reaction reaction);

    List<ReactionDto> toDto(List<Reaction> reactionList);

    Set<ReactionDto> toDto(Set<Reaction> reactionSet);
}
