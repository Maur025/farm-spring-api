package com.kernotec.farm.command.person;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.jpa.dto.entity.PersonDto;
import com.kernotec.farm.account.jpa.dto.mapper.PersonDtoMapper;
import com.kernotec.farm.account.jpa.entity.Person;
import com.kernotec.farm.account.jpa.service.PersonService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PersonGetDtoCmd extends
    AbstractTransactionalRequiredCommand<PersonGetDtoCmd.Request, PersonDto>
{

    private final PersonService personService;
    private final PersonDtoMapper personDtoMapper;

    @Override
    protected PersonDto run(Request request) {
        Person person = personService.findByIdThrow(request.getPersonId());
        return personDtoMapper.toDto(person);
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final UUID personId;
    }
}
