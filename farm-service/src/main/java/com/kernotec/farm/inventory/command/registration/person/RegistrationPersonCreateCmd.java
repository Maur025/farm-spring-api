package com.kernotec.farm.inventory.command.registration.person;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
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
public class RegistrationPersonCreateCmd extends
    AbstractTransactionalRequiredCommand<RegistrationPersonCreateCmd.Request, UUID>
{

    private final RegistrationPersonService registrationPersonService;

    @Override
    protected UUID run(Request request) {
        RegistrationPerson registrationPerson = new RegistrationPerson();

        registrationPerson.setDocumentNumber(request.getDocumentNumber());
        registrationPerson.setPersonId(request.getPersonId());

        registrationPerson = registrationPersonService.save(registrationPerson);
        return registrationPerson.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String documentNumber;
        @NotNull
        private final UUID personId;
    }
}
