package com.kernotec.farm.rest.dto.response.chip;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.rest.dto.response.operator.OperatorResponse;
import com.kernotec.farm.rest.dto.response.registration.person.RegistrationPersonResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ChipForAccountResponse extends EntityResponse {

    private String phoneNumber;
    private boolean isDeviceInside;

    private UUID operatorId;
    private OperatorResponse operator;

    private UUID registrationPersonId;
    private RegistrationPersonResponse registrationPerson;
}
