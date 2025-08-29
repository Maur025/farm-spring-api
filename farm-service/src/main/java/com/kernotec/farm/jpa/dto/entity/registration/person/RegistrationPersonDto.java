package com.kernotec.farm.jpa.dto.entity.registration.person;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.jpa.dto.entity.person.PersonDto;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationPersonDto extends AuditEntityDto {

    private String documentNumber;

    private UUID personId;
    private PersonDto person;
}
