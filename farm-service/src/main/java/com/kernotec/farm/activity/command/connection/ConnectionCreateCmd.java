package com.kernotec.farm.activity.command.connection;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.jpa.entity.Connection;
import com.kernotec.farm.activity.jpa.enums.ConnectionActionEnum;
import com.kernotec.farm.activity.jpa.enums.ConnectionTypeEnum;
import com.kernotec.farm.activity.jpa.service.ConnectionService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConnectionCreateCmd extends
    AbstractTransactionalRequiredCommand<ConnectionCreateCmd.Request, UUID>
{

    private final ConnectionService connectionService;

    @Override
    protected UUID run(Request request) {
        Connection connection = new Connection();

        connection.setPotentialFriendAccountId(request.getPotentialFriendAccountId());
        connection.setAction(request.getAction());
        connection.setType(request.getType());
        connection.setRequestStateId(request.getRequestStateId());
        connection.setActivityId(request.getActivityId());
        connection.setActivityTypeId(request.getActivityTypeId());

        connection = connectionService.save(connection);
        return connection.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID potentialFriendAccountId;
        @NotNull
        private final ConnectionActionEnum action;
        @NotNull
        private final ConnectionTypeEnum type;
        @NotNull
        private final UUID requestStateId;
        @NotNull
        private final UUID activityId;
        @NotNull
        private final UUID activityTypeId;
    }
}
