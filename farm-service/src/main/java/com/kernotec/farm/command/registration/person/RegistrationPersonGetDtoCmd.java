package com.kernotec.farm.command.registration.person;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.inventory.jpa.dto.entity.RegistrationPersonDto;
import com.kernotec.farm.inventory.jpa.dto.mapper.RegistrationPersonDtoMapper;
import com.kernotec.farm.inventory.jpa.entity.RegistrationPerson;
import com.kernotec.farm.inventory.jpa.service.RegistrationPersonService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RegistrationPersonGetDtoCmd extends
    AbstractTransactionalRequiredCommand<RegistrationPersonGetDtoCmd.Request, RegistrationPersonDto>
{

    private final RegistrationPersonService registrationPersonService;
    private final RegistrationPersonDtoMapper registrationPersonDtoMapper;

    @Override
    protected RegistrationPersonDto run(Request request) {
        RegistrationPerson registrationPerson = registrationPersonService.findByIdThrow(
            request.getRegistrationPersonId());
        return registrationPersonDtoMapper.toDto(registrationPerson);
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID registrationPersonId;
    }
}
