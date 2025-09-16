package com.kernotec.farm.account.jpa.dto.entity;

import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObservationDto {

    private String description;
    private ZonedDateTime dateObserved;
}
