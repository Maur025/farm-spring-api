package com.kernotec.farm.activity.command.connection;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.activity.jpa.dto.entity.ConnectionDto;
import com.kernotec.farm.activity.jpa.dto.mapper.ConnectionDtoMapper;
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
public class ConnectionGetDtoCmd extends
    AbstractTransactionalRequiredCommand<ConnectionGetDtoCmd.Request, ConnectionDto>
{

    private final ConnectionService connectionService;
    private final ConnectionDtoMapper connectionDtoMapper;

    @Override
    protected ConnectionDto run(Request request) {
        Connection connection = connectionService.findByIdThrow(request.getConnectionId());
        return connectionDtoMapper.toDto(connection);
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID connectionId;
    }
}
