package com.kernotec.farm.jpa.dto.mapper.registration.person;

import com.kernotec.farm.jpa.dto.entity.registration.person.RegistrationPersonDto;
import com.kernotec.farm.jpa.entity.RegistrationPerson;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface RegistrationPersonDtoMapper {

    RegistrationPersonDto toDto(RegistrationPerson registrationPerson);

    List<RegistrationPersonDto> toDto(List<RegistrationPerson> registrationPersonList);

    Set<RegistrationPersonDto> toDto(Set<RegistrationPerson> registrationPersonSet);
}
