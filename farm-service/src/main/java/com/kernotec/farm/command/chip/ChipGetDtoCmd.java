package com.kernotec.farm.command.chip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.jpa.dto.entity.chip.ChipDto;
import com.kernotec.farm.jpa.dto.mapper.chip.ChipDtoMapper;
import com.kernotec.farm.jpa.entity.Chip;
import com.kernotec.farm.jpa.service.ChipService;
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
