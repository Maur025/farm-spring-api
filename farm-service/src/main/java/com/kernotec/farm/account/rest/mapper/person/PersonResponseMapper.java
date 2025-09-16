package com.kernotec.farm.account.rest.mapper.person;

import com.kernotec.farm.account.jpa.entity.Person;
import com.kernotec.farm.account.rest.dto.response.person.PersonResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface PersonResponseMapper {

    PersonResponse toResponse(Person person);

    PersonResponse toResponse(UUID id);

    List<PersonResponse> toResponse(List<Person> personList);

    Set<PersonResponse> toResponse(Set<Person> personSet);
}
