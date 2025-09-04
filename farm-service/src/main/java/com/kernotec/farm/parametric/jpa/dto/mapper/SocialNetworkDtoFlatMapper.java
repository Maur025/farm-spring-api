package com.kernotec.farm.parametric.jpa.dto.mapper;

import com.kernotec.farm.parametric.jpa.dto.entity.SocialNetworkDto;
import com.kernotec.farm.parametric.jpa.entity.SocialNetwork;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SocialNetworkDtoFlatMapper {

    @Mapping(target = "accounts", ignore = true)
    SocialNetworkDto toDto(SocialNetwork socialNetwork);

    List<SocialNetworkDto> toDto(List<SocialNetwork> socialNetworkList);

    Set<SocialNetworkDto> toDto(Set<SocialNetwork> socialNetworkSet);
}
