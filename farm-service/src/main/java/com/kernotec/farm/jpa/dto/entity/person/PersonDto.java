package com.kernotec.farm.jpa.dto.entity.person;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDto extends AuditEntityDto {

    private String name;
    private String lastName;
    private LocalDateTime birthDate;
}
