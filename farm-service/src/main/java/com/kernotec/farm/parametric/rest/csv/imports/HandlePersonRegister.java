package com.kernotec.farm.parametric.rest.csv.imports;

import com.kernotec.farm.account.command.person.PersonCreateManyCmd;
import com.kernotec.farm.account.jpa.entity.Person;
import com.kernotec.farm.account.jpa.service.PersonService;
import com.kernotec.farm.inventory.command.registration.person.RegistrationPersonCreateManyCmd;
import com.kernotec.farm.inventory.jpa.entity.RegistrationPerson;
import com.kernotec.farm.inventory.jpa.service.RegistrationPersonService;
import com.kernotec.farm.parametric.rest.csv.common.CsvImportCommon;
import com.kernotec.farm.parametric.rest.csv.dto.request.PersonRegisterRequest;
import com.kernotec.farm.parametric.rest.csv.enums.HandleTypeEnum;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HandlePersonRegister implements HandleImport<PersonRegisterRequest> {

    private final CsvImportCommon csvImportCommon;
    private final PersonService personService;
    private final PersonCreateManyCmd personCreateManyCmd;
    private final RegistrationPersonService registrationPersonService;
    private final RegistrationPersonCreateManyCmd registrationPersonCreateManyCmd;

    @Override
    public HandleTypeEnum handleType() {
        return HandleTypeEnum.PERSON_REGISTER;
    }

    @Override
    public Map<String, UUID> handle(PersonRegisterRequest request) {
        Map<String, Person> personRegisterMap = request.importExcelDataDtoBatch()
            .stream()
            .collect(Collectors.toMap(
                dto -> csvImportCommon.getPersonCode(
                    dto.getPersonRegisterName(), dto.getPersonRegisterLastName()),
                dto -> csvImportCommon.getPersonEntity(
                    dto.getPersonRegisterName(),
                    dto.getPersonRegisterLastName()
                ), (existing, replacement) -> replacement
            ));

        List<String> personNameAndLastNames = personRegisterMap.values()
            .stream()
            .map(person -> csvImportCommon.getPersonCode(person.getName(), person.getLastName()))
            .toList();

        Map<String, UUID> personRegisterMatchMap = personService.findAllByNameAndLastNameInList(
                personNameAndLastNames)
            .stream()
            .collect(Collectors.toMap(
                person -> csvImportCommon.getPersonCode(person.getName(), person.getLastName()),
                Person::getId, (existing, replacement) -> existing
            ));

        List<Person> personListToSave = new ArrayList<>();
        for (Person person : personRegisterMap.values()) {
            if (personRegisterMatchMap.containsKey(
                csvImportCommon.getPersonCode(person.getName(), person.getLastName())))
            {
                continue;
            }

            personListToSave.add(person);
        }

        if (!personListToSave.isEmpty()) {
            List<Person> personListSaved = personCreateManyCmd.withRequest(
                    PersonCreateManyCmd.Request.builder()
                        .personList(personListToSave)
                        .build())
                .execute();

            for (Person person : personListSaved) {
                personRegisterMatchMap.put(
                    csvImportCommon.getPersonCode(person.getName(), person.getLastName()),
                    person.getId()
                );
            }
        }

        Map<UUID, String> inversePersonRegisterMatchMap = personRegisterMatchMap.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        Map<UUID, RegistrationPerson> registrationPersonMemoryMap = personRegisterMatchMap.values()
            .stream()
            .collect(
                Collectors.toMap(
                    personId -> personId, this::getRegistrationPersonEntity,
                    (existing, replacement) -> existing
                ));

        Set<UUID> personIds = new HashSet<>(personRegisterMatchMap.values());

        Map<UUID, UUID> registrationPersonMatchMap = registrationPersonService.findAllByPersonIdIn(
                personIds)
            .stream()
            .collect(
                Collectors.toMap(
                    RegistrationPerson::getPersonId, RegistrationPerson::getId,
                    (existing, replacement) -> existing
                ));

        List<RegistrationPerson> registrationPersonListToSave = new ArrayList<>();

        for (RegistrationPerson registrationPerson : registrationPersonMemoryMap.values()) {
            if (registrationPersonMatchMap.containsKey(registrationPerson.getPersonId())) {
                continue;
            }

            registrationPersonListToSave.add(registrationPerson);
        }

        if (!registrationPersonListToSave.isEmpty()) {
            List<RegistrationPerson> registrationPersonListSaved = registrationPersonCreateManyCmd.withRequest(
                    RegistrationPersonCreateManyCmd.Request.builder()
                        .registrationPersonList(registrationPersonListToSave)
                        .build())
                .execute();

            for (RegistrationPerson registrationPerson : registrationPersonListSaved) {
                registrationPersonMatchMap.put(
                    registrationPerson.getPersonId(), registrationPerson.getId());
            }
        }

        return registrationPersonMatchMap.entrySet()
            .stream()
            .filter(entry -> inversePersonRegisterMatchMap.containsKey(entry.getKey()))
            .collect(Collectors.toMap(
                entry -> inversePersonRegisterMatchMap.get(entry.getKey()),
                Map.Entry::getValue
            ));
    }

    private RegistrationPerson getRegistrationPersonEntity(UUID personId)
    {
        var registrationPerson = new RegistrationPerson();

        registrationPerson.setPersonId(personId);
        registrationPerson.setDocumentNumber("N/A");

        return registrationPerson;
    }
}
