package com.kernotec.farm.activity.rest.command.connection;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.friend.FriendCreateCmd;
import com.kernotec.farm.activity.command.connection.ConnectionGetDtoCmd;
import com.kernotec.farm.activity.command.connection.ConnectionUpdateCmd;
import com.kernotec.farm.activity.jpa.dto.entity.ConnectionDto;
import com.kernotec.farm.activity.rest.dto.request.connection.ConnectionUpdateRequest;
import com.kernotec.farm.parametric.jpa.entity.FriendState;
import com.kernotec.farm.parametric.jpa.entity.RequestState;
import com.kernotec.farm.parametric.jpa.enums.FriendStateCodeEnum;
import com.kernotec.farm.parametric.jpa.enums.RequestStateCodeEnum;
import com.kernotec.farm.parametric.jpa.service.FriendStateService;
import com.kernotec.farm.parametric.jpa.service.RequestStateService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessConnectionUpdateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessConnectionUpdateRequestCmd.Request, Void>
{

    private final RequestStateService requestStateService;
    private final ConnectionUpdateCmd connectionUpdateCmd;
    private final ConnectionGetDtoCmd connectionGetDtoCmd;
    private final FriendCreateCmd friendCreateCmd;
    private final FriendStateService friendStateService;

    @Override
    protected Void run(Request request) {
        ConnectionUpdateRequest connectionRequest = request.getConnectionRequest();

        RequestState requestState = requestStateService.findByCodeThrow(
            RequestStateCodeEnum.fromValue(connectionRequest.getResponseRequestState()));

        ConnectionDto connectionDto = connectionGetDtoCmd.withRequest(
                ConnectionGetDtoCmd.Request.builder()
                    .connectionId(request.getConnectionId())
                    .build())
            .execute();

        FriendState friendState = friendStateService.findByCodeThrow(FriendStateCodeEnum.ACTIVE);

        friendCreateCmd.withRequest(FriendCreateCmd.Request.builder()
                .acceptedAt(connectionRequest.getResponseDate())
                .accountId(connectionDto.getActivity()
                    .getAccountId())
                .friendAccountId(connectionDto.getPotentialFriendAccountId())
                .friendStateId(friendState.getId())
                .build())
            .execute();

        friendCreateCmd.withRequest(FriendCreateCmd.Request.builder()
                .acceptedAt(connectionRequest.getResponseDate())
                .accountId(connectionDto.getPotentialFriendAccountId())
                .friendAccountId(connectionDto.getActivity()
                    .getAccountId())
                .friendStateId(friendState.getId())
                .build())
            .execute();

        connectionUpdateCmd.withRequest(ConnectionUpdateCmd.Request.builder()
                .connectionId(request.getConnectionId())
                .requestStateId(requestState.getId())
                .build())
            .execute();

        return null;
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID connectionId;

        @NotNull
        private final ConnectionUpdateRequest connectionRequest;
    }
}
