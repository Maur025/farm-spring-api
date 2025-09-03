package com.kernotec.farm.account.jpa.dto.mapper;

import com.kernotec.farm.account.jpa.dto.entity.PersonDto;
import com.kernotec.farm.account.jpa.entity.Person;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface PersonDtoMapper {

    PersonDto toDto(Person person);

    List<PersonDto> toDto(List<Person> personList);

    Set<PersonDto> toDto(Set<Person> personSet);
}
