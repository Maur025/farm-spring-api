package com.kernotec.farm.account.command.friend;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.dto.entity.FriendDto;
import com.kernotec.farm.account.jpa.dto.mapper.FriendDtoMapper;
import com.kernotec.farm.account.jpa.entity.Friend;
import com.kernotec.farm.account.jpa.service.FriendService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FriendGetDtoCmd extends
    AbstractTransactionalRequiredCommand<FriendGetDtoCmd.Request, FriendDto>
{

    private final FriendService friendService;
    private final FriendDtoMapper friendDtoMapper;

    @Override
    protected FriendDto run(Request request) {
        Friend friend = friendService.findByIdThrow(request.getFriendId());
        return friendDtoMapper.toDto(friend);
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID friendId;
    }
}
