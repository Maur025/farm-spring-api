package com.kernotec.farm.account.rest.dto.response.person;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kernotec.core.rest.dto.response.data.EntityResponse;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonResponse extends EntityResponse {

    private String name;
    private String lastName;
    private LocalDateTime birthDate;
}
