package com.kernotec.farm.parametric.command.friend.state;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.parametric.jpa.enums.FriendStateCodeEnum;
import com.kernotec.farm.parametric.jpa.service.FriendStateService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FriendStateGetIdByCodeCmd extends
    AbstractTransactionalRequiredCommand<FriendStateGetIdByCodeCmd.Request, UUID>
{

    private final FriendStateService friendStateService;

    @Override
    protected UUID run(Request request) {
        return friendStateService.findByCodeThrow(request.getCode())
            .getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final FriendStateCodeEnum code;
    }
}
