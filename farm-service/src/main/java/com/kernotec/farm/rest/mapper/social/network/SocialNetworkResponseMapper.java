package com.kernotec.farm.rest.mapper.social.network;

import com.kernotec.farm.jpa.entity.SocialNetwork;
import com.kernotec.farm.rest.dto.response.social.network.SocialNetworkResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface SocialNetworkResponseMapper {

    SocialNetworkResponse toResponse(UUID id);

    SocialNetworkResponse toResponse(SocialNetwork socialNetwork);

    List<SocialNetworkResponse> toResponse(List<SocialNetwork> socialNetworkList);

    Set<SocialNetworkResponse> toResponse(Set<SocialNetwork> socialNetworkSet);
}
