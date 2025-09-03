package com.kernotec.farm.rest.mapper.friend;

import com.kernotec.farm.jpa.entity.Friend;
import com.kernotec.farm.rest.dto.response.friend.FriendResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface FriendResponseMapper {

    FriendResponse toResponse(UUID id);

    FriendResponse toResponse(Friend friend);

    List<FriendResponse> toResponse(List<Friend> friendList);

    Set<FriendResponse> toResponse(Set<Friend> friendSet);
}
