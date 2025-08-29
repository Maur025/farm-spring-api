package com.kernotec.farm.command.operator;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.jpa.entity.Operator;
import com.kernotec.farm.jpa.enums.OperatorCodeEnum;
import com.kernotec.farm.jpa.service.OperatorService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OperatorCreateCmd extends
    AbstractTransactionalRequiredCommand<OperatorCreateCmd.Request, UUID>
{

    private final OperatorService operatorService;

    @Override
    protected UUID run(Request request) {
        Operator operator = new Operator();

        operator.setName(request.getName());
        operator.setCode(request.getCode());

        operator = operatorService.save(operator);
        return operator.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String name;
        @NotNull
        private final OperatorCodeEnum code;
    }
}
