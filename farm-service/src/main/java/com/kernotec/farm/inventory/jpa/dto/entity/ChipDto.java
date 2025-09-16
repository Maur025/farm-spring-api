package com.kernotec.farm.inventory.jpa.dto.entity;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.account.jpa.dto.entity.AccountDto;
import com.kernotec.farm.parametric.jpa.dto.entity.OperatorDto;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChipDto extends AuditEntityDto {

    private String phoneNumber;
    private boolean isDeviceInside;

    private UUID operatorId;
    private OperatorDto operator;

    private UUID registrationPersonId;
    private RegistrationPersonDto registrationPerson;

    private UUID deviceId;
    private DeviceDto device;

    private Set<AccountDto> accounts;
}
