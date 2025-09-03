package com.kernotec.farm.inventory.command.chip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.inventory.jpa.dto.entity.ChipDto;
import com.kernotec.farm.inventory.jpa.dto.mapper.ChipDtoMapper;
import com.kernotec.farm.inventory.jpa.entity.Chip;
import com.kernotec.farm.inventory.jpa.service.ChipService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChipGetDtoCmd extends
    AbstractTransactionalRequiredCommand<ChipGetDtoCmd.Request, ChipDto>
{

    private final ChipService chipService;
    private final ChipDtoMapper chipDtoMapper;

    @Override
    protected ChipDto run(Request request) {
        Chip chip = chipService.findByIdThrow(request.getChipId());
        return chipDtoMapper.toDto(chip);
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID chipId;
    }
}
