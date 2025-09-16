package com.kernotec.farm.inventory.rest.command.registration.person;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.inventory.command.registration.person.RegistrationPersonCreateCmd;
import com.kernotec.farm.inventory.command.registration.person.RegistrationPersonGetDtoCmd;
import com.kernotec.farm.inventory.jpa.dto.entity.RegistrationPersonDto;
import com.kernotec.farm.inventory.jpa.entity.RegistrationPerson;
import com.kernotec.farm.inventory.jpa.service.RegistrationPersonService;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RegistrationPersonFindOrCreateCmd extends
    AbstractTransactionalRequiredCommand<RegistrationPersonFindOrCreateCmd.Request, RegistrationPersonDto>
{

    private final RegistrationPersonService registrationPersonService;
    private final RegistrationPersonGetDtoCmd registrationPersonGetDtoCmd;
    private final RegistrationPersonCreateCmd registrationPersonCreateCmd;

    @Override
    protected RegistrationPersonDto run(Request request) {
        Optional<RegistrationPerson> registrationPerson = registrationPersonService.findByPersonId(
            request.getPersonId());

        UUID registrationPersonId = registrationPerson.map(RegistrationPerson::getId)
            .orElseGet(() -> registrationPersonCreateCmd.withRequest(
                    RegistrationPersonCreateCmd.Request.builder()
                        .personId(request.getPersonId())
                        .documentNumber(
                            request.getDocumentNumber() == null ? "N/A" : request.getDocumentNumber())
                        .build())
                .execute());

        return registrationPersonGetDtoCmd.withRequest(RegistrationPersonGetDtoCmd.Request.builder()
                .registrationPersonId(registrationPersonId)
                .build())
            .execute();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID personId;
        private final String documentNumber;
    }
}
