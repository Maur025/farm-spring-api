package com.kernotec.farm.rest.command.chip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.command.chip.ChipCreateCmd;
import com.kernotec.farm.command.chip.ChipGetDtoCmd;
import com.kernotec.farm.command.registration.person.RegistrationPersonCreateCmd;
import com.kernotec.farm.jpa.dto.entity.chip.ChipDto;
import com.kernotec.farm.jpa.entity.Operator;
import com.kernotec.farm.jpa.enums.OperatorCodeEnum;
import com.kernotec.farm.jpa.service.OperatorService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChipAssignRegisterCmd extends
    AbstractTransactionalRequiredCommand<ChipAssignRegisterCmd.Request, ChipDto>
{

    private final ChipGetDtoCmd chipGetDtoCmd;
    private final ChipCreateCmd chipCreateCmd;
    private final OperatorService operatorService;
    private final RegistrationPersonCreateCmd registrationPersonCreateCmd;

    @Override
    protected ChipDto run(Request request) {
        Operator operator = operatorService.findByCodeThrow(
            OperatorCodeEnum.fromValue(request.getOperatorCode()));

        UUID registrationPersonId = registrationPersonCreateCmd.withRequest(
                RegistrationPersonCreateCmd.Request.builder()
                    .personId(request.getPersonId())
                    .documentNumber(
                        request.getDocumentNumber() == null ? "N/A" : request.getDocumentNumber())
                    .build())
            .execute();

        UUID chipId = chipCreateCmd.withRequest(ChipCreateCmd.Request.builder()
                .phoneNumber(request.getPhoneNumber())
                .isDeviceInside(
                    request.getIsDeviceInside() == null ? Boolean.FALSE : request.getIsDeviceInside())
                .operatorId(operator.getId())
                .registrationPersonId(registrationPersonId)
                .deviceId(request.getDeviceId())
                .build())
            .execute();

        return chipGetDtoCmd.withRequest(ChipGetDtoCmd.Request.builder()
                .chipId(chipId)
                .build())
            .execute();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String phoneNumber;
        private final Boolean isDeviceInside;
        private final String operatorCode;
        @NotNull
        private final UUID personId;
        private final String documentNumber;
        @NotNull
        private final UUID deviceId;
    }
}
