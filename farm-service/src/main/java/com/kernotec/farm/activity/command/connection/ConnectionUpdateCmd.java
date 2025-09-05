package com.kernotec.farm.activity.command.connection;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.jpa.entity.Connection;
import com.kernotec.farm.activity.jpa.service.ConnectionService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConnectionUpdateCmd extends
    AbstractTransactionalRequiredCommand<ConnectionUpdateCmd.Request, Void>
{

    private final ConnectionService connectionService;

    @Override
    protected Void run(Request request) {
        Connection connection = connectionService.findByIdThrow(request.getConnectionId());

        if (request.getRequestStateId() != null) {
            connection.setRequestStateId(request.getRequestStateId());
        }

        connectionService.save(connection);
        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID connectionId;

        private final UUID requestStateId;
    }
}
