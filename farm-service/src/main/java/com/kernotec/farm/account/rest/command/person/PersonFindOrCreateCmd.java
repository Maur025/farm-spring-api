package com.kernotec.farm.account.rest.command.person;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farm.account.command.person.PersonCreateCmd;
import com.kernotec.farm.account.command.person.PersonGetDtoCmd;
import com.kernotec.farm.account.jpa.dto.entity.PersonDto;
import com.kernotec.farm.account.jpa.entity.Person;
import com.kernotec.farm.account.jpa.service.PersonService;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Deprecated(since = "v.of.2025-02-27")
public class PersonFindOrCreateCmd extends
    AbstractTransactionalRequiredCommand<PersonFindOrCreateCmd.Request, PersonDto>
{

    private final PersonService personService;
    private final PersonGetDtoCmd personGetDtoCmd;
    private final PersonCreateCmd personCreateCmd;

    @Override
    protected PersonDto run(Request request) {
        Optional<Person> person = personService.findByNameAndLastName(
            request.getName(),
            request.getLastName()
        );

        UUID personId = person.map(Person::getId)
            .orElseGet(() -> personCreateCmd.withRequest(PersonCreateCmd.Request.builder()
                    .name(request.getName())
                    .lastName(request.getLastName())
                    .birthDate(request.getBirthDateString() == null ? null
                        : getBirthDate(request.getBirthDateString()))
                    .build())
                .execute());

        return personGetDtoCmd.withRequest(PersonGetDtoCmd.Request.builder()
                .personId(personId)
                .build())
            .execute();
    }

    private LocalDateTime getBirthDate(String birthDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthDateLocalDate = LocalDate.parse(birthDateString, formatter);

        return birthDateLocalDate.atStartOfDay();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String name;
        @NotNull
        private final String lastName;
        private final String birthDateString;
    }
}
