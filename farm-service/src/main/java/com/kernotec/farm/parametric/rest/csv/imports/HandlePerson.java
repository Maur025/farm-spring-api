package com.kernotec.farm.parametric.rest.csv.imports;

import com.kernotec.farm.account.command.person.PersonCreateManyCmd;
import com.kernotec.farm.account.jpa.entity.Person;
import com.kernotec.farm.account.jpa.service.PersonService;
import com.kernotec.farm.parametric.rest.csv.common.CsvImportCommon;
import com.kernotec.farm.parametric.rest.csv.dto.request.PersonRequest;
import com.kernotec.farm.parametric.rest.csv.enums.HandleTypeEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HandlePerson implements HandleImport<PersonRequest> {

    private final CsvImportCommon csvImportCommon;
    private final PersonService personService;
    private final PersonCreateManyCmd personCreateManyCmd;

    @Override
    public HandleTypeEnum handleType() {
        return HandleTypeEnum.PERSON;
    }

    @Override
    public Map<String, UUID> handle(PersonRequest request) {
        Map<String, Person> personFakeMap = request.importExcelDataDtoBatch()
            .stream()
            .collect(Collectors.toMap(
                dto -> csvImportCommon.getPersonCode(
                    dto.getPersonFakeName(), dto.getPersonFakeLastName()),
                dto -> csvImportCommon.getPersonEntity(
                    dto.getPersonFakeName(),
                    dto.getPersonFakeLastName()
                ), (existing, replacement) -> existing
            ));

        List<String> nameAndLastNameSearchList = personFakeMap.values()
            .stream()
            .map(person -> csvImportCommon.getPersonCode(person.getName(), person.getLastName()))
            .toList();

        Map<String, UUID> personMatchMap = personService.findAllByNameAndLastNameInList(
                nameAndLastNameSearchList)
            .getContent()
            .stream()
            .collect(Collectors.toMap(
                person -> csvImportCommon.getPersonCode(person.getName(), person.getLastName()),
                Person::getId
            ));

        List<Person> personListToSave = new ArrayList<>();

        for (Person person : personFakeMap.values()) {
            if (personMatchMap.containsKey(
                csvImportCommon.getPersonCode(person.getName(), person.getLastName())))
            {
                continue;
            }

            personListToSave.add(person);
        }

        if (personListToSave.isEmpty()) {
            return personMatchMap;
        }

        List<Person> personListSaved = personCreateManyCmd.withRequest(
                PersonCreateManyCmd.Request.builder()
                    .personList(personListToSave)
                    .build())
            .execute();

        for (Person person : personListSaved) {
            personMatchMap.put(
                csvImportCommon.getPersonCode(person.getName(), person.getLastName()),
                person.getId()
            );
        }

        return personMatchMap;
    }
}
