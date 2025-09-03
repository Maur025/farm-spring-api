package com.kernotec.farm.inventory.rest.dto.response.registration.person;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import com.kernotec.farm.account.rest.dto.response.person.PersonResponse;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class RegistrationPersonResponse extends EntityResponse {

    private String documentNumber;

    private UUID personId;
    private PersonResponse person;
}
