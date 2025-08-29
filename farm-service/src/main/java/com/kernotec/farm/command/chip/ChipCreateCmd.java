package com.kernotec.farm.command.chip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
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
public class ChipCreateCmd extends
    AbstractTransactionalRequiredCommand<ChipCreateCmd.Request, UUID>
{

    private final ChipService chipService;

    @Override
    protected UUID run(Request request) {
        Chip chip = new Chip();

        chip.setPhoneNumber(request.getPhoneNumber());
        chip.setDeviceInside(request.isDeviceInside());
        chip.setOperatorId(request.getOperatorId());
        chip.setRegistrationPersonId(request.getRegistrationPersonId());
        chip.setDeviceId(request.getDeviceId());

        chip = chipService.save(chip);
        return chip.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String phoneNumber;
        @NotNull
        private final boolean isDeviceInside;
        @NotNull
        private final UUID operatorId;
        @NotNull
        private final UUID registrationPersonId;
        @NotNull
        private final UUID deviceId;
    }
}
