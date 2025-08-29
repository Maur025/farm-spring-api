package com.kernotec.farm.rest.command.farm;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.command.farm.FarmCreateCmd;
import com.kernotec.farm.command.farm.FarmGetDtoCmd;
import com.kernotec.farm.jpa.dto.entity.farm.FarmDto;
import com.kernotec.farm.jpa.entity.Farm;
import com.kernotec.farm.jpa.enums.FarmTypeEnum;
import com.kernotec.farm.jpa.service.FarmService;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FarmFindOrCreateCmd extends
    AbstractTransactionalRequiredCommand<FarmFindOrCreateCmd.Request, FarmDto>
{

    private final FarmGetDtoCmd farmGetDtoCmd;
    private final FarmService farmService;
    private final FarmCreateCmd farmCreateCmd;

    @Override
    protected FarmDto run(Request request) {
        Optional<Farm> farm = farmService.findByCode(request.getCode());

        UUID farmId = farm.map(Farm::getId)
            .orElseGet(() -> farmCreateCmd.withRequest(FarmCreateCmd.Request.builder()
                    .name(request.getName())
                    .code(request.getCode())
                    .description(request.getDescription())
                    .type(FarmTypeEnum.fromValue(request.getType()))
                    .build())
                .execute());

        return farmGetDtoCmd.withRequest(FarmGetDtoCmd.Request.builder()
                .farmId(farmId)
                .build())
            .execute();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String code;
        @NotNull
        private final String name;
        @NotNull
        private final String type;
        private final String description;
    }
}
