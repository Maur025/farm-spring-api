package com.kernotec.farm.command.person;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.entity.Person;
import com.kernotec.farm.account.jpa.service.PersonService;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PersonCreateCmd extends
    AbstractTransactionalRequiredCommand<PersonCreateCmd.Request, UUID>
{

    private final PersonService personService;

    @Override
    protected UUID run(Request request) {
        Person person = new Person();

        person.setName(request.getName());
        person.setLastName(request.getLastName());
        person.setBirthDate(request.getBirthDate());

        person = personService.save(person);
        return person.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String name;
        @NotNull
        private final String lastName;
        private final LocalDateTime birthDate;
    }
}
