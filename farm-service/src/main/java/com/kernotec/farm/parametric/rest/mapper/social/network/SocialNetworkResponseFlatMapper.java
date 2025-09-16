package com.kernotec.farm.parametric.rest.mapper.social.network;

import com.kernotec.farm.parametric.jpa.entity.SocialNetwork;
import com.kernotec.farm.parametric.rest.dto.response.social.network.SocialNetworkResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SocialNetworkResponseFlatMapper {

    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "activityTypes", ignore = true)
    SocialNetworkResponse toResponse(SocialNetwork socialNetwork);

    SocialNetworkResponse toResponse(UUID id);

    List<SocialNetworkResponse> toResponse(List<SocialNetwork> socialNetworkList);

    Set<SocialNetworkResponse> toResponse(Set<SocialNetwork> socialNetworkSet);
}
