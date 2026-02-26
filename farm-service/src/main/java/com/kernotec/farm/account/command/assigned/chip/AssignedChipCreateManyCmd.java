package com.kernotec.farm.account.command.assigned.chip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.AssignedChip;
import com.kernotec.farm.account.jpa.service.AssignedChipService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AssignedChipCreateManyCmd extends
    AbstractTransactionalRequiredCommand<AssignedChipCreateManyCmd.Request, List<AssignedChip>>
{

    private final AssignedChipService assignedChipService;

    @Override
    protected List<AssignedChip> run(Request request) {
        if (request.assignedChipList.isEmpty()) {
            log.debug("No found assigned chip to create");
            return null;
        }

        return assignedChipService.saveAll(request.assignedChipList);
    }

    @Builder
    public record Request(@NotNull List<AssignedChip> assignedChipList) {

    }
}
