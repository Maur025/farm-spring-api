package com.kernotec.farm.inventory.command.registration.person;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.inventory.jpa.entity.RegistrationPerson;
import com.kernotec.farm.inventory.jpa.service.RegistrationPersonService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegistrationPersonCreateManyCmd extends
    AbstractTransactionalRequiredCommand<RegistrationPersonCreateManyCmd.Request, List<RegistrationPerson>>
{

    private final RegistrationPersonService registrationPersonService;

    @Override
    protected List<RegistrationPerson> run(Request request) {
        if (request.registrationPersonList.isEmpty()) {
            log.debug("No found registration person to create");
            return null;
        }

        return registrationPersonService.saveAll(request.registrationPersonList);
    }

    @Builder
    public record Request(@NotNull List<RegistrationPerson> registrationPersonList) {

    }
}
