package com.kernotec.farm.inventory.jpa.dto.mapper;

import com.kernotec.farm.inventory.jpa.dto.entity.RegistrationPersonDto;
import com.kernotec.farm.inventory.jpa.entity.RegistrationPerson;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface RegistrationPersonDtoMapper {

    RegistrationPersonDto toDto(RegistrationPerson registrationPerson);

    List<RegistrationPersonDto> toDto(List<RegistrationPerson> registrationPersonList);

    Set<RegistrationPersonDto> toDto(Set<RegistrationPerson> registrationPersonSet);
}
