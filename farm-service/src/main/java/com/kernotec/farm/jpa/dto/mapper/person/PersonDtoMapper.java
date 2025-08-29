package com.kernotec.farm.jpa.dto.mapper.person;

import com.kernotec.farm.jpa.dto.entity.person.PersonDto;
import com.kernotec.farm.jpa.entity.Person;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface PersonDtoMapper {

    PersonDto toDto(Person person);

    List<PersonDto> toDto(List<Person> personList);

    Set<PersonDto> toDto(Set<Person> personSet);
}
