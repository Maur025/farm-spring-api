package com.kernotec.farm.account.jpa.dto.mapper;

import com.kernotec.farm.account.jpa.dto.entity.FriendDto;
import com.kernotec.farm.account.jpa.entity.Friend;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = {AccountDtoFlatMapper.class})
public interface FriendDtoMapper {

    FriendDto toDto(Friend friend);

    List<FriendDto> toDto(List<Friend> friendList);

    Set<Friend> toDto(Set<Friend> friendSet);
}
