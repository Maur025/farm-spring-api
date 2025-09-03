package com.kernotec.farm.inventory.command.farm;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.inventory.jpa.dto.entity.FarmDto;
import com.kernotec.farm.inventory.jpa.dto.mapper.FarmDtoMapper;
import com.kernotec.farm.inventory.jpa.entity.Farm;
import com.kernotec.farm.inventory.jpa.service.FarmService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FarmGetDtoCmd extends
    AbstractTransactionalRequiredCommand<FarmGetDtoCmd.Request, FarmDto>
{

    private final FarmService farmService;
    private final FarmDtoMapper farmDtoMapper;

    @Override
    protected FarmDto run(Request request) {
        Farm farm = farmService.findByIdThrow(request.getFarmId());
        return farmDtoMapper.toDto(farm);
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID farmId;
    }
}
