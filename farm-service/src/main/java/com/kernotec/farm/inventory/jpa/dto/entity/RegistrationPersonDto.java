package com.kernotec.farm.inventory.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.account.jpa.dto.entity.PersonDto;
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
