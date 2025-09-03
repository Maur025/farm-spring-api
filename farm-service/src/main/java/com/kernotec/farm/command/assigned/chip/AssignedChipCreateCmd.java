package com.kernotec.farm.command.assigned.chip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.AssignedChip;
import com.kernotec.farm.account.jpa.service.AssignedChipService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AssignedChipCreateCmd extends
    AbstractTransactionalRequiredCommand<AssignedChipCreateCmd.Request, UUID>
{

    private final AssignedChipService assignedChipService;

    @Override
    protected UUID run(Request request) {
        AssignedChip assignedChip = new AssignedChip();

        assignedChip.setChipId(request.getChipId());
        assignedChip.setAccountId(request.getAccountId());

        assignedChip = assignedChipService.save(assignedChip);
        return assignedChip.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID chipId;
        @NotNull
        private final UUID accountId;
    }
}
