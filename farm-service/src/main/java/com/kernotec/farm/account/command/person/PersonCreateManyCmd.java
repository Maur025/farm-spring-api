package com.kernotec.farm.account.command.person;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.Person;
import com.kernotec.farm.account.jpa.service.PersonService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonCreateManyCmd extends
    AbstractTransactionalRequiredCommand<PersonCreateManyCmd.Request, List<Person>>
{

    private final PersonService personService;

    @Override
    protected List<Person> run(Request request) {
        if (request.personList.isEmpty()) {
            log.debug("No found person to create");
            return null;
        }

        return personService.saveAll(request.personList);
    }

    @Builder
    public record Request(@NotNull List<Person> personList) {

    }
}
