package com.kernotec.farm.inventory.command.farm;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.inventory.jpa.entity.Farm;
import com.kernotec.farm.inventory.jpa.service.FarmService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FarmCreateManyCmd extends
    AbstractTransactionalRequiredCommand<FarmCreateManyCmd.Request, List<Farm>>
{

    private final FarmService farmService;

    @Override
    protected List<Farm> run(Request request) {
        if (request.farmList.isEmpty()) {
            log.debug("No found farms to save");
            return null;
        }

        return farmService.saveAll(request.farmList);
    }

    @Builder
    public record Request(@NotNull List<Farm> farmList) {

    }
}
