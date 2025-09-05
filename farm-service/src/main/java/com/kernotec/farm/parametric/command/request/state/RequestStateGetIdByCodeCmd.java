package com.kernotec.farm.parametric.command.request.state;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.parametric.jpa.enums.RequestStateCodeEnum;
import com.kernotec.farm.parametric.jpa.service.RequestStateService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RequestStateGetIdByCodeCmd extends
    AbstractTransactionalRequiredCommand<RequestStateGetIdByCodeCmd.Request, UUID>
{

    private final RequestStateService requestStateService;

    @Override
    protected UUID run(Request request) {
        return requestStateService.findByCodeThrow(request.getCode())
            .getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final RequestStateCodeEnum code;
    }
}
