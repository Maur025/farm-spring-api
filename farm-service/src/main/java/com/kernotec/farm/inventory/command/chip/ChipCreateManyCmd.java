package com.kernotec.farm.inventory.command.chip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.inventory.jpa.entity.Chip;
import com.kernotec.farm.inventory.jpa.service.ChipService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChipCreateManyCmd extends
    AbstractTransactionalRequiredCommand<ChipCreateManyCmd.Request, List<Chip>>
{

    private final ChipService chipService;

    @Override
    protected List<Chip> run(Request request) {
        if (request.chipList.isEmpty()) {
            log.debug("No found device to create.");
            return null;
        }

        return chipService.saveAll(request.chipList);
    }

    @Builder
    public record Request(@NotNull List<Chip> chipList) {

    }
}
