package com.kernotec.farm.command.farm;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.jpa.entity.Farm;
import com.kernotec.farm.jpa.enums.FarmTypeEnum;
import com.kernotec.farm.jpa.service.FarmService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FarmCreateCmd extends
    AbstractTransactionalRequiredCommand<FarmCreateCmd.Request, UUID>
{

    private final FarmService farmService;

    @Override
    protected UUID run(Request request) {
        Farm farm = new Farm();

        farm.setName(request.getName());
        farm.setCode(request.getCode());
        farm.setDescription(request.getDescription());
        farm.setType(request.getType());

        farm = farmService.save(farm);
        return farm.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String name;
        @NotNull
        private final String code;
        private final String description;
        private final FarmTypeEnum type;
    }
}
