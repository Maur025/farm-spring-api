package com.kernotec.farm.account.rest.mapper.friend;

import com.kernotec.farm.account.jpa.entity.Friend;
import com.kernotec.farm.account.rest.dto.response.friend.FriendResponse;
import com.kernotec.farm.account.rest.mapper.account.AccountResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {AccountResponseFlatMapper.class})
public interface FriendResponseMapper {

    FriendResponse toResponse(UUID id);

    FriendResponse toResponse(Friend friend);

    List<FriendResponse> toResponse(List<Friend> friendList);

    Set<FriendResponse> toResponse(Set<Friend> friendSet);
}
