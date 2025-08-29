package com.kernotec.farm.jpa.dto.entity.chip;

import com.kernotec.core.jpa.dto.AuditEntityDto;
import com.kernotec.farm.jpa.dto.entity.device.DeviceDto;
import com.kernotec.farm.jpa.dto.entity.operator.OperatorDto;
import com.kernotec.farm.jpa.dto.entity.registration.person.RegistrationPersonDto;
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
}
