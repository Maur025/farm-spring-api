package com.kernotec.farm.activity.jpa.service;

import com.kernotec.farm.activity.jpa.enums.ConnectionActionEnum;
import com.kernotec.farm.parametric.command.request.state.RequestStateGetIdByCodeCmd;
import com.kernotec.farm.parametric.jpa.enums.RequestStateCodeEnum;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConnectionHandleService {

    private final RequestStateGetIdByCodeCmd requestStateGetIdByCodeCmd;

    public UUID getReqStatePendingOrApprovedIdByAction(ConnectionActionEnum action) {
        if (action.equals(ConnectionActionEnum.INCOMING_FRIEND_REQUEST_AND_CONFIRMED)) {
            return requestStateGetIdByCodeCmd.withRequest(
                    RequestStateGetIdByCodeCmd.Request.builder()
                        .code(RequestStateCodeEnum.APPROVED)
                        .build())
                .execute();
        }

        return requestStateGetIdByCodeCmd.withRequest(RequestStateGetIdByCodeCmd.Request.builder()
                .code(RequestStateCodeEnum.PENDING)
                .build())
            .execute();
    }
}
